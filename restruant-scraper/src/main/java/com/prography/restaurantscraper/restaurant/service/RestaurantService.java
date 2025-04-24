package com.prography.restaurantscraper.restaurant.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.restaurantscraper.external.KakaoFeignClient;
import com.prography.restaurantscraper.restaurant.domain.RawRestaurantData;
import com.prography.restaurantscraper.restaurant.dto.PlaceData;
import com.prography.restaurantscraper.restaurant.dto.KakaoPlaceResponse;
import com.prography.restaurantscraper.restaurant.dto.ScrapScaleDto;
import com.prography.restaurantscraper.restaurant.repository.RawRestaurantDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@Slf4j
public class RestaurantService {

    private static final double SMALL_SCALE_FACTOR = 0.002;
    private static final String FOOD_CATEGORY_CODE = "FD6";
    private static final String DELIMITER = "@@";
    private static final int PAGE_MAX_SIZE = 15;
    private static final String ID_SEPARATOR = "@";
    private static final int THREAD_SLEEP_MILLIS = 10_000;
    private static final int KAKAO_PAGE_SIZE_LIMIT = 45;


    private final ObjectMapper objectMapper;
    private final Executor executor;
    private final KakaoFeignClient kakaoFeignClient;
    private final RawRestaurantDataRepository rawRestaurantDataRepository;

    public RestaurantService(
        ObjectMapper objectMapper, @Qualifier("scrapExecutor") Executor executor,
        KakaoFeignClient kakaoFeignClient,
        RawRestaurantDataRepository rawRestaurantDataRepository) {
        this.objectMapper = objectMapper;
        this.executor = executor;
        this.kakaoFeignClient = kakaoFeignClient;
        this.rawRestaurantDataRepository = rawRestaurantDataRepository;
    }

    public void scrapAllRestaurants(ScrapScaleDto scrapScaleDto) {
        double minLat = scrapScaleDto.getMin_latitude();
        double maxLat = scrapScaleDto.getMax_latitude();
        double minLng = scrapScaleDto.getMin_longitude();
        double maxLng = scrapScaleDto.getMax_longitude();

        List<CompletableFuture<Void>> futures = new ArrayList<>();


        for (double lat = minLat; lat < maxLat; lat += SMALL_SCALE_FACTOR) {
            for (double lng = minLng; lng < maxLng; lng += SMALL_SCALE_FACTOR) {
                double fromLat = lat;
                double toLat = Math.min(lat + SMALL_SCALE_FACTOR, maxLat);
                double fromLng = lng;
                double toLng = Math.min(lng + SMALL_SCALE_FACTOR, maxLng);
                futures.add(searchDataAsync(fromLat, fromLng, toLat, toLng));
            }
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        System.out.println("All restaurant scraping tasks completed.");
    }


    private CompletableFuture<Void> searchDataAsync(double fromLat, double fromLng, double toLat, double toLng) {
        // 스레드 실행
        return CompletableFuture.runAsync(() -> {
            try {
                log.info("check All From " + fromLat + " " + fromLng + " " + toLat + " " + toLng);
                List<PlaceData> placeList = getAllRestaurantsInRect(
                    toRectParam(fromLat, fromLng, toLat, toLng));
                log.info("Found " + placeList.size() + " restaurant data.");
                placeList.forEach(this::savePlace);
                sleepThread();
            } catch (Exception e) {
                System.err.println(
                    "Error in range: " + fromLat + "," + fromLng + " - " + e.getMessage());
            }
        }, executor);
    }

    private void sleepThread() throws InterruptedException {
        Thread.sleep(THREAD_SLEEP_MILLIS);
    }

    private List<PlaceData> getAllRestaurantsInRect(String rectParam) {
        List<PlaceData> kakaoPlaces = new ArrayList<>();
        int page = 1;
        KakaoPlaceResponse kakaoPlaceResponse = kakaoFeignClient.searchByCategory(
            FOOD_CATEGORY_CODE, // 음식점 category code
            page,
            rectParam
        );
        if (kakaoPlaceResponse.getMeta().getTotal_count() > 45) {
            log.error("Place Size is {} (in {})", kakaoPlaceResponse.getMeta().getTotal_count(),
                rectParam);
        }
        kakaoPlaces.addAll(kakaoPlaceResponse.getDocuments());
        int endPage = (int) Math.ceil((double) kakaoPlaceResponse.getMeta().getTotal_count() / PAGE_MAX_SIZE);

        for (page = 2; page <= endPage; page++) {
            kakaoPlaces.addAll(
                kakaoFeignClient.searchByCategory(
                    FOOD_CATEGORY_CODE, // 음식점 category code
                    page,
                    rectParam
                ).getDocuments()
            );
        }
        return kakaoPlaces;
    }

    private String toRectParam(double fromLat, double fromLng, double toLat, double toLng) {
        return String.format("%f,%f,%f,%f", fromLat, fromLng, toLat, toLng);
    }

    private void savePlace(PlaceData placeData) {
        String id = getIdFromPlaceData(placeData);

        if (isAlreadySaved(id)) {
            log.info("Document with id {} already exists.", id);
            return;
        }
        persistPlace(id, placeData);
    }

    private String getIdFromPlaceData(PlaceData placeData) {
        String id = placeData.getAddress_name() + ID_SEPARATOR + placeData.getPlace_name();
        return id.replace(" ", "_");
    }

    private boolean isAlreadySaved(String docId) {
        return rawRestaurantDataRepository.existsById(docId);
    }

    private void persistPlace(String docId, PlaceData placeData) {
        try {
            String placeJson = objectMapper.writeValueAsString(placeData);
            RawRestaurantData data = new RawRestaurantData(docId, placeJson);
            rawRestaurantDataRepository.save(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize place data", e);
        }
    }


    /**
     * 기존 문서에 리뷰 정보 추가 (place@@reviews 구조로)
     */
    public void appendReviewsToPlace(String id, String reviewsJson) {
        RawRestaurantData existing = rawRestaurantDataRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Place not found with id: " + id));

        String updatedData = existing.getData() + DELIMITER + reviewsJson;
        existing.setData(updatedData);

        rawRestaurantDataRepository.save(existing);
    }
}


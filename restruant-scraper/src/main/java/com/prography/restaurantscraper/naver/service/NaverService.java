package com.prography.restaurantscraper.naver.service;

import static com.prography.restaurantscraper.common.constant.RawDataConstants.DATA_SPLITTER;

import com.fasterxml.jackson.databind.JsonNode;
import com.prography.restaurantscraper.common.external.NaverFeignClient;
import com.prography.restaurantscraper.kakaoReview.dto.KakaoReviewResponse;
import com.prography.restaurantscraper.naver.dto.PlaceItem;
import com.prography.restaurantscraper.naver.dto.PlaceSearchResponse;
import com.prography.restaurantscraper.restaurant.dto.PlaceData;
import com.prography.restaurantscraper.restaurant.repository.CustomRawRestaurantRepository;
import com.prography.restaurantscraper.restaurant.repository.RawRestaurantDataRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NaverService {
    private final Executor executor;
    private static final String NAVER_KEYWORD = "naverPlaceInfo";
    private final NaverFeignClient naverFeignClient;
    private final RawRestaurantDataRepository rawRestaurantDataRepository;
    private final CustomRawRestaurantRepository customRawRestaurantRepository;

    public NaverService(@Qualifier("scrapExecutor") Executor executor, NaverFeignClient naverFeignClient,
        RawRestaurantDataRepository rawRestaurantDataRepository,
        CustomRawRestaurantRepository customRawRestaurantRepository) {
        this.executor = executor;
        this.naverFeignClient = naverFeignClient;
        this.rawRestaurantDataRepository = rawRestaurantDataRepository;
        this.customRawRestaurantRepository = customRawRestaurantRepository;
    }

    public void searchAllReviewsAsync() {
        List<CompletableFuture<Void>> futures = rawRestaurantDataRepository.findAll().stream()
            .map(rawRestaurantData -> CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(10000);
                    log.info("naverSearchStart = {}", rawRestaurantData.getId());
                    Optional<PlaceData> placeData = customRawRestaurantRepository.getPlaceDataFromValue(
                        "placeData", rawRestaurantData.getData());
                    if (placeData.isEmpty()) {
                        return;
                    }
                    Optional<PlaceItem> naverInfo = findNaverInfo(placeData.get());
                    if (naverInfo.isEmpty()) {
                        return;
                    }
                    log.info("NaverInfo: {}", naverInfo.get().getAddress());
                    customRawRestaurantRepository.patchOrSaveData(rawRestaurantData, NAVER_KEYWORD,
                        naverInfo.get());
                } catch (Exception e) {
                    log.error("Failed to process review for id: {}", rawRestaurantData.getId(), e);
                }
            }, executor))
            .toList();
        // 모든 작업 완료까지 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        log.info("✅ All review updates completed.");
    }

    private Optional<PlaceItem> findNaverInfo(PlaceData placeData) {
        String query = placeData.getAddress_name() + " " + placeData.getPlace_name();
        String coord = placeData.getX() + ";" + placeData.getY();
        PlaceSearchResponse searchResponse = naverFeignClient.searchNaverInfo(query, "all", coord);
        List<PlaceItem> placeList = searchResponse.getResult().getPlace().getList();
        if (placeList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(placeList.getFirst());
    }
}

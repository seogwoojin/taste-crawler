package com.prography.restaurantscraper.kakaoReview.service;

import static com.prography.restaurantscraper.common.constant.RawDataConstants.DATA_SPLITTER;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.restaurantscraper.common.external.KakaoReviewFeignClient;
import com.prography.restaurantscraper.kakaoReview.dto.KakaoReviewResponse;
import com.prography.restaurantscraper.kakaoReview.dto.Order;
import com.prography.restaurantscraper.restaurant.domain.RawRestaurantData;
import com.prography.restaurantscraper.restaurant.dto.PlaceData;
import com.prography.restaurantscraper.restaurant.repository.RawRestaurantDataRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoReviewService {

    @Qualifier("scrapExecutor") private final Executor executor;
    private final KakaoReviewFeignClient kakaoReviewClient;
    private final RawRestaurantDataRepository rawRestaurantDataRepository;
    private final ObjectMapper objectMapper;

    public void searchAllReviewsAsync() {
        List<CompletableFuture<Void>> futures = rawRestaurantDataRepository.findAll().stream()
            .map(data -> CompletableFuture.runAsync(() -> {
                try {
                    Optional<PlaceData> placeDataOpt = getPlaceDataFromValue(data.getData());
                    if (placeDataOpt.isEmpty()) return;

                    PlaceData placeData = placeDataOpt.get();
                    KakaoReviewResponse response = searchReviewsByKakaoId(placeData.getId());
                    saveOrUpdateKakaoReviews(data, response);
                } catch (Exception e) {
                    log.error("Failed to process review for id: {}", data.getId(), e);
                }
            }, executor))
            .toList();
        // 모든 작업 완료까지 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        log.info("✅ All review updates completed.");
    }


    private void saveOrUpdateKakaoReviews(RawRestaurantData rawRestaurantData, KakaoReviewResponse kakaoReviewResponse) {
        String[] parts = rawRestaurantData.getData().split(DATA_SPLITTER);

        List<String> newParts = replaceOrAppendJson(parts, "kakaoReviewData", kakaoReviewResponse);

        rawRestaurantData.setData(String.join(DATA_SPLITTER, newParts));
        rawRestaurantDataRepository.save(rawRestaurantData);
    }

    private List<String> replaceOrAppendJson(String[] parts, String key, Object valueObject) {
        List<String> updatedParts = new ArrayList<>();
        boolean updated = false;

        for (String part : parts) {
            try {
                JsonNode root = objectMapper.readTree(part);
                if (root.has(key)) {
                    String newJson = objectMapper.writeValueAsString(Map.of(key, valueObject));
                    updatedParts.add(newJson);
                    updated = true;
                } else {
                    updatedParts.add(part);
                }
            } catch (Exception e) {
                log.warn("Failed to process JSON during replaceOrAppend", e);
                updatedParts.add(part);
            }
        }

        if (!updated) {
            try {
                String newJson = objectMapper.writeValueAsString(Map.of(key, valueObject));
                updatedParts.add(newJson);
            } catch (JsonProcessingException e) {
                log.error("Failed to serialize new value object", e);
            }
        }

        return updatedParts;
    }

    private Optional<PlaceData> getPlaceDataFromValue(String rawData) {
        String[] split = rawData.split(DATA_SPLITTER);
        return Arrays.stream(split)
            .map(jsonData -> {
                try {
                    JsonNode root = objectMapper.readTree(jsonData);
                    if (root.has("placeData")) {
                        return objectMapper.treeToValue(root.get("placeData"), PlaceData.class);
                    }
                } catch (Exception e) {
                    log.warn("Failed to parse part of split JSON: {}", jsonData, e);
                }
                return null;
            })
            .filter(Objects::nonNull)
            .findFirst(); // placeData가 포함된 첫 번째 JSON 추출
    }

    private KakaoReviewResponse searchReviewsByKakaoId(String kakaoId) {
        long previousLasReviewId = 0;
        KakaoReviewResponse kakaoReviewResponse = kakaoReviewClient.searchReviewsByKakaoId(
            previousLasReviewId, Order.LATEST, false, kakaoId
        );
        if (kakaoReviewResponse.isHas_next()) {
            previousLasReviewId = kakaoReviewResponse.getReviews().getLast().getReview_id();
            KakaoReviewResponse nextReviewResponse = kakaoReviewClient.searchReviewsByKakaoId(
                previousLasReviewId, Order.LATEST, false,
                kakaoId);
            kakaoReviewResponse.getReviews().addAll(nextReviewResponse.getReviews());
        }
        log.info("Review size : {}", kakaoReviewResponse.getReviews().size());
        return kakaoReviewResponse;
    }
}

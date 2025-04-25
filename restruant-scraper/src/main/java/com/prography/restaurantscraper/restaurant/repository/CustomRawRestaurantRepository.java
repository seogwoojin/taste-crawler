package com.prography.restaurantscraper.restaurant.repository;

import static com.prography.restaurantscraper.common.constant.RawDataConstants.DATA_SPLITTER;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.restaurantscraper.restaurant.domain.RawRestaurantData;
import com.prography.restaurantscraper.restaurant.dto.PlaceData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomRawRestaurantRepository {

    private final ObjectMapper objectMapper;
    private final RawRestaurantDataRepository rawRestaurantDataRepository;

    public void patchOrSaveData(RawRestaurantData rawRestaurantData, String keyword, Object valueObject) {
        String[] parts = rawRestaurantData.getData().split(DATA_SPLITTER);

        List<String> newParts = replaceOrAppendJson(parts, keyword, valueObject);

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


    public Optional<PlaceData> getPlaceDataFromValue(String keyword, String rawData) {
        String[] split = rawData.split(DATA_SPLITTER);
        return Arrays.stream(split)
            .map(jsonData -> {
                try {
                    JsonNode root = objectMapper.readTree(jsonData);
                    if (root.has(keyword)) {
                        return objectMapper.treeToValue(root.get(keyword), PlaceData.class);
                    }
                } catch (Exception e) {
                    log.warn("Failed to parse part of split JSON: {}", jsonData, e);
                }
                return null;
            })
            .filter(Objects::nonNull)
            .findFirst(); // placeData가 포함된 첫 번째 JSON 추출
    }


}

package com.prography.restaurantscraper.restaurant.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.restaurantscraper.external.KakaoFeignClient;
import com.prography.restaurantscraper.restaurant.domain.RawRestaurantData;
import com.prography.restaurantscraper.restaurant.dto.KakaoPlaceResponse;
import com.prography.restaurantscraper.restaurant.dto.ScrapScaleDto;
import com.prography.restaurantscraper.restaurant.repository.RawRestaurantDataRepository;
import com.prography.restaurantscraper.restaurant.service.RestaurantService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KakaoScraperController {
    private final RestaurantService restaurantService;
    private final RawRestaurantDataRepository rawRestaurantDataRepository;

    @PostMapping("/save-restaurant")
    public ResponseEntity<?> saveRestaurant(@RequestBody ScrapScaleDto scrapScaleDto) {
        restaurantService.scrapAllRestaurants(scrapScaleDto);
        return ResponseEntity.ok("Restaurant saved successfully.");
    }

    @GetMapping("/list")
    public ResponseEntity<List<Map<String, Object>>> getAllRestaurants()
        throws JsonProcessingException {
        List<RawRestaurantData> all = rawRestaurantDataRepository.findAll();
        List<Map<String, Object>> parsed = new ArrayList<>();

        for (RawRestaurantData raw : all) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = objectMapper.readValue(raw.getData(), Map.class);
            map.put("_id", raw.getId()); // ID 포함
            parsed.add(map);
        }

        return ResponseEntity.ok(parsed);
    }
}

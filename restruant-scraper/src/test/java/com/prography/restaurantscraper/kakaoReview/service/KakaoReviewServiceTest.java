package com.prography.restaurantscraper.kakaoReview.service;

import static org.junit.jupiter.api.Assertions.*;

import com.prography.restaurantscraper.restaurant.dto.ScrapScaleDto;
import com.prography.restaurantscraper.restaurant.service.RestaurantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KakaoReviewServiceTest {
    @Autowired private KakaoReviewService kakaoReviewService;
    @Autowired private RestaurantService restaurantService;

    @Test
    void searchAllReviews() {
        kakaoReviewService.searchAllReviewsAsync();
    }

    @Test
    @DisplayName("강남역 주변 음식점 검색")
    void searchReviews(){
        ScrapScaleDto gangnamGu = new ScrapScaleDto();
        gangnamGu.setMin_latitude(37.4960);
        gangnamGu.setMin_longitude(127.0245);
        gangnamGu.setMax_latitude(37.4977);
        gangnamGu.setMax_longitude(127.0267);
        restaurantService.scrapAllRestaurants(gangnamGu);

    }
}
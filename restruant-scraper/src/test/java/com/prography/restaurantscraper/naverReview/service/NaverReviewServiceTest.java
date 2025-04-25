package com.prography.restaurantscraper.naverReview.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NaverReviewServiceTest {

    @Autowired
    private NaverReviewService naverReviewService;

    @Test
    void searchAllReviewsAsync() {
        naverReviewService.searchAllReviewsAsync();
    }
}
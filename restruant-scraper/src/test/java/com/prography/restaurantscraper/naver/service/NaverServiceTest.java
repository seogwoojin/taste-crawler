package com.prography.restaurantscraper.naver.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NaverServiceTest {

    @Autowired
    private NaverService naverService;

    @Test
    public void naverServiceTest() {
        naverService.searchAllReviewsAsync();
    }
}
package com.prography.restaurantscraper.common.external;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NaverReviewFeignConfig {
    @Bean
    public RequestInterceptor naverReviewHeaderInterceptor() {
        final String ua = "Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1";
        return template -> {
            template.header("User-Agent", ua);
            template.header("Referer", "https://m.place.naver.com");
        };
    }
}

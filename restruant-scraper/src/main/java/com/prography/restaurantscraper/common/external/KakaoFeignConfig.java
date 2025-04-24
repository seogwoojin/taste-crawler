package com.prography.restaurantscraper.common.external;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakaoFeignConfig {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> template.header("Authorization", "KakaoAK " + kakaoApiKey);
    }
}

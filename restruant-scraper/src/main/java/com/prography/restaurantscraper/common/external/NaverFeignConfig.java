package com.prography.restaurantscraper.common.external;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

class NaverFeignConfig {

    private static final String UA =
        "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) "
      + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Mobile Safari/537.36a";

    @Bean // ‼️ @Component(전역)로 두지 말고, 이 Config 안에 한정
    RequestInterceptor naverHeaderInterceptor() {
        return template -> {
            template.header("User-Agent", UA);
            template.header("Referer",  "https://map.naver.com");
        };
    }
}
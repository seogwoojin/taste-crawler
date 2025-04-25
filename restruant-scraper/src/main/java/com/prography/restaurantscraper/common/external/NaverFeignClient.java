package com.prography.restaurantscraper.common.external;

import com.prography.restaurantscraper.naver.dto.PlaceSearchResponse;
import com.prography.restaurantscraper.restaurant.dto.KakaoPlaceResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "naverClient",
    url = "https://map.naver.com",
    configuration = NaverFeignConfig.class
)
public interface NaverFeignClient {

    @GetMapping(
        value = "/p/api/search/allSearch"   // 응답 MIME
    )
    PlaceSearchResponse searchNaverInfo(
        /* ───────── Query Parameters ───────── */
        @RequestParam("query") String query,        // 검색어 (자동 URL-encoding)
        @RequestParam("type") String type,         // 보통 "all"
        @RequestParam("searchCoord") String searchCoord   // "lng;lat"
    );
}

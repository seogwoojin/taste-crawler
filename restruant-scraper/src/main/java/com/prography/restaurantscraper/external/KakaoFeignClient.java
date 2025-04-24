package com.prography.restaurantscraper.external;

import com.prography.restaurantscraper.restaurant.dto.KakaoPlaceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "kakaoClient",
        url = "https://dapi.kakao.com",
        configuration = KakaoFeignConfig.class
)
public interface KakaoFeignClient {

    @GetMapping("/v2/local/search/category.json")
    KakaoPlaceResponse searchByCategory(
            @RequestParam("category_group_code") String categoryGroupCode,
            @RequestParam("page") int page,
            @RequestParam("rect") String rect
    );
}

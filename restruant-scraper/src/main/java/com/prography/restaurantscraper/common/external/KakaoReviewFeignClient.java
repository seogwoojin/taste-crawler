package com.prography.restaurantscraper.common.external;

import com.prography.restaurantscraper.kakaoReview.dto.KakaoReviewResponse;
import com.prography.restaurantscraper.kakaoReview.dto.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "kakaoReviewClient",
    url = "https://place-api.map.kakao.com",
    configuration = KakaoReviewFeignConfig.class
)
public interface KakaoReviewFeignClient {
    @GetMapping("/places/tab/reviews/kakaomap/{kakaoId}")
    KakaoReviewResponse searchReviewsByKakaoId(
        @RequestParam("previous_last_review_id") long previousLastReviewId,
        @RequestParam("order") Order order,
        @RequestParam("only_photo_review") Boolean onlyPhotoReview,
        @PathVariable String kakaoId);
}

package com.prography.restaurantscraper.common.external;

import com.prography.restaurantscraper.naver.dto.PlaceSearchResponse;
import com.prography.restaurantscraper.naverReview.dto.GraphQLEnvelope;
import com.prography.restaurantscraper.naverReview.dto.GraphQLRequest;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "naverReviewClient",
    url = "https://pcmap-api.place.naver.com",
    configuration = NaverReviewFeignConfig.class
)
public interface NaverReviewFeignClient {
    @PostMapping(
        value    = "/graphql"
    )
    List<GraphQLEnvelope> getVisitorReviews(@RequestBody List<GraphQLRequest> body);
}

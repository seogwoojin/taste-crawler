package com.prography.restaurantscraper.naverReview.service;

import static com.prography.restaurantscraper.common.constant.RawDataConstants.NAVER_KEYWORD;
import static com.prography.restaurantscraper.common.constant.RawDataConstants.NAVER_REVIEW_KEYWORD;

import com.prography.restaurantscraper.common.external.NaverFeignClient;
import com.prography.restaurantscraper.common.external.NaverReviewFeignClient;
import com.prography.restaurantscraper.naver.dto.PlaceItem;
import com.prography.restaurantscraper.naverReview.dto.GraphQLEnvelope;
import com.prography.restaurantscraper.naverReview.dto.GraphQLRequest;
import com.prography.restaurantscraper.naverReview.dto.GraphQLVariables;
import com.prography.restaurantscraper.naverReview.dto.VisitorReviewsInput;
import com.prography.restaurantscraper.restaurant.dto.PlaceData;
import com.prography.restaurantscraper.restaurant.repository.CustomRawRestaurantRepository;
import com.prography.restaurantscraper.restaurant.repository.RawRestaurantDataRepository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NaverReviewService {

    private static final String REVIEW_QUERY =
        "query getVisitorReviews($input: VisitorReviewsInput) {"
            + "  visitorReviews(input: $input) {"
            + "    total "
            + "    starDistribution { score count } "
            + "    items { "
            + "      id reviewId originType created language translatedText "
            + "      rating body tags "
            + "      media { type thumbnail videoUrl } "
            + "      author { id nickname imageUrl } "
            + "      viewCount visitCount "
            + "      reactionStat { totalCount typeCount { name count } } "
            + "      hasViewerReacted { reacted } "
            + "    } "
            + "  } "
            + "}";

    private final Executor executor;
    private final NaverReviewFeignClient naverReviewFeignClient;
    private final RawRestaurantDataRepository rawRestaurantDataRepository;
    private final CustomRawRestaurantRepository customRawRestaurantRepository;

    public NaverReviewService(@Qualifier("scrapExecutor") Executor executor,
        NaverReviewFeignClient naverReviewFeignClient,
        RawRestaurantDataRepository rawRestaurantDataRepository,
        CustomRawRestaurantRepository customRawRestaurantRepository) {
        this.executor = executor;
        this.naverReviewFeignClient = naverReviewFeignClient;
        this.rawRestaurantDataRepository = rawRestaurantDataRepository;
        this.customRawRestaurantRepository = customRawRestaurantRepository;
    }

    public void searchAllReviewsAsync() {
        List<CompletableFuture<Void>> futures = rawRestaurantDataRepository.findAll().stream()
            .map(rawRestaurantData -> CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(10000);
                    log.info("naverReviewSearchStart = {}", rawRestaurantData.getId());
                    Optional<PlaceItem> placeItem = customRawRestaurantRepository.getNaverDataFromValue(
                        NAVER_KEYWORD, rawRestaurantData.getData());
                    System.out.println("placeItem = " + placeItem.isEmpty());
                    if (placeItem.isEmpty()) {
                        return;
                    }
                    List<GraphQLEnvelope> naverReviewInfo = findNaverInfo(placeItem.get().getId());
                    if (naverReviewInfo.isEmpty()) {
                        return;
                    }
                    log.info("naverReviewSearchResult = {}", naverReviewInfo.get(0).getData().getVisitorReviews().getTotal());
                    customRawRestaurantRepository.patchOrSaveData(rawRestaurantData, NAVER_REVIEW_KEYWORD,
                        naverReviewInfo.get(0).getData());
                } catch (Exception e) {
                    log.error("Failed to process review for id: {}", rawRestaurantData.getId(), e);
                }
            }, executor))
            .toList();
        // 모든 작업 완료까지 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        log.info("✅ All review updates completed.");
    }

    public List<GraphQLEnvelope> findNaverInfo(String businessId) {

        /* 1) VisitorReviewsInput 생성 */
        VisitorReviewsInput input = new VisitorReviewsInput(
            businessId,                 // placeId
            "restaurant",               // businessType
            1,                          // page
            10                          // size
        );

        /* 2) variables 래퍼 객체 */
        GraphQLVariables variables = new GraphQLVariables(input);

        /* 3) GraphQLRequest 구성 */
        GraphQLRequest request = new GraphQLRequest();
        request.setOperationName("getVisitorReviews");
        request.setQuery(REVIEW_QUERY);
        request.setVariables(variables);

        /* 4) Feign 호출 (배치 → List 한 건) */
        return naverReviewFeignClient.getVisitorReviews(List.of(request));
    }
}

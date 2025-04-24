package com.prography.restaurantscraper.kakaoReview.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoReviewResponse {
    private ScoreSet score_set;
    private List<StrengthDescription> strength_description;
    private List<Review> reviews;
    private TimelineScoreTable timeline_score_table;
    private boolean has_next;
    private boolean has_my_review;
}

package com.prography.restaurantscraper.kakaoReview.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreSet {
    private int review_count;
    private int total_score;
    private double average_score;
    private List<StrengthCount> strength_counts;
    private int strength_uv;
}


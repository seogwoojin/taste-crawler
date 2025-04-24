package com.prography.restaurantscraper.kakaoReview.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Review {
    private long review_id;
    private int star_rating;
    private String contents;
    private int photo_count;
    private String status;
    private List<Integer> strength_ids;
    private String registered_at;
    private String updated_at;
    private ReviewMeta meta;
    private List<ReviewPhoto> photos;
}

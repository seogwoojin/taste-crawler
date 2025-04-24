package com.prography.restaurantscraper.kakaoReview.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewPhoto {
    private String url;
    private long photo_id;
    private long review_id;
    private String updated_at;
    private ReviewPhotoMeta meta;
}

package com.prography.restaurantscraper.kakaoReview.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewPhotoMeta {
    private ReviewOwner owner;
    private boolean is_liked_by_me;
    private int view_count;
    private int like_count;
    private boolean near;
    private Place place;
}

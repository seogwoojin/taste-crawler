package com.prography.restaurantscraper.kakaoReview.dto;


import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewMeta {
    private ReviewOwner owner;
    private boolean is_liked_by_me;
    private boolean is_owner_me;
    private int like_count;
    private List<String> like_user_profile_image_list;
    private boolean is_place_owner_pick;
    private Place place;
}

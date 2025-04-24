package com.prography.restaurantscraper.kakaoReview.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewOwner {
    private String map_user_id;
    private String nickname;
    private String profile_image_url;
    private TimelineLevel timeline_level;
    private int review_count;
    private int follower_count;
    private double average_score;
    private String profile_status;
    private boolean is_following;
}

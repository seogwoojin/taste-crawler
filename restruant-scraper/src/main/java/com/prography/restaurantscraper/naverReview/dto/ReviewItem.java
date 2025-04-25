package com.prography.restaurantscraper.naverReview.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewItem {
    private String id;
    private String reviewId;
    private String originType;       // "영수증" 등
    private Integer rating;          // null 가능
    private String body;
    private List<String> tags;       // null 가능
    private List<Media> media;
    private Author author;
    private int viewCount;
    private int visitCount;
    private ViewerReaction hasViewerReacted;
}
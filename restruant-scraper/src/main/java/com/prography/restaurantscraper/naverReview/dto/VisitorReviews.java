package com.prography.restaurantscraper.naverReview.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitorReviews {
    private int total;
    private List<StarDistribution> starDistribution;   // null 가능
    private List<ReviewItem> items;
}

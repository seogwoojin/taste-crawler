package com.prography.restaurantscraper.naverReview.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Media {
    private String type;             // image / video
    private String thumbnail;
    private String videoUrl;         // null when image
}
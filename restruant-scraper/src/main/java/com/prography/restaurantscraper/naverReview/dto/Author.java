package com.prography.restaurantscraper.naverReview.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {
    private String id;
    private String nickname;
    private String imageUrl;         // null
}

package com.prography.restaurantscraper.naver.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Status {
    private Integer code;
    private String text;
    private Boolean emphasis;
    private String description;
    private String detailInfo;
}

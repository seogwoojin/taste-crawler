package com.prography.restaurantscraper.naver.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BusinessStatus {
    private String requestTime;
    private Status status;
    private String businessHours;
    private String breakTime;
    private String lastOrder;
}
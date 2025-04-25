package com.prography.restaurantscraper.naver.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 최상위 응답 ─ naverPlaceClient.searchPlace()
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PlaceSearchResponse {

    private Result result;
}
package com.prography.restaurantscraper.naver.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Collections;
import java.util.List;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Place {
    private List<PlaceItem> list = Collections.emptyList();
}
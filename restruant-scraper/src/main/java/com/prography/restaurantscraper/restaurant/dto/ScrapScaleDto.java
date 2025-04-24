package com.prography.restaurantscraper.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ScrapScaleDto {
    private double min_latitude;
    private double max_latitude;
    private double min_longitude;
    private double max_longitude;
}

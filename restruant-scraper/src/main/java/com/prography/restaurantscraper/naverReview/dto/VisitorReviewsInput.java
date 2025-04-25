package com.prography.restaurantscraper.naverReview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitorReviewsInput {
    private String businessId;     // 1876859078
    private String businessType;   // restaurant
    private int    page;           // 1
    private int    size;           // 50
}
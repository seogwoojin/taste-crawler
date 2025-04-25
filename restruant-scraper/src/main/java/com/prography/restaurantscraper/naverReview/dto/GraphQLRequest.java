package com.prography.restaurantscraper.naverReview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphQLRequest {
    private String            operationName;  // "getVisitorReviews"
    private GraphQLVariables  variables;       // 위 클래스
    private String            query;           // GraphQL 쿼리 문자열
}
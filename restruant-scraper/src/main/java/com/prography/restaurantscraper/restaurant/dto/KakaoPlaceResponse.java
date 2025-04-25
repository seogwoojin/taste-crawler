package com.prography.restaurantscraper.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// KakaoPlaceResponse.java
@Getter
@Setter
public class KakaoPlaceResponse {
    private List<PlaceData> documents;
    private KakaoMeta meta;

    // getters and setters
    public List<PlaceData> getDocuments() { return documents; }
    public void setDocuments(List<PlaceData> documents) { this.documents = documents; }

    public KakaoMeta getMeta() { return meta; }
    public void setMeta(KakaoMeta meta) { this.meta = meta; }
}

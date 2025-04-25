package com.prography.restaurantscraper.naver.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PlaceItem {

    /* 기본 식별/표시 */
    private String index;
    private String rank;
    private String id;               // "163636452"
    private String name;
    private String display;

    /* 연락처 */
    private String tel;
    private String telDisplay;
    private Boolean isCallLink;
    private String virtualTel;
    private String virtualTelDisplay;

    /* 카테고리 */
    private List<String> category;

    /* 위치 */
    private String address;
    private String roadAddress;
    private String abbrAddress;
    private List<String> shortAddress;
    private String x;
    private String y;
    private String distance;

    /* 미디어/리뷰 */
    private String thumUrl;
    private List<String> thumUrls;
    private Integer reviewCount;
    private Integer placeReviewCount;
    private List<String> microReview;

    /* 영업 정보 */
    private BusinessStatus businessStatus;
    private String bizhourInfo;
    private String menuInfo;

    /* 파노라마/마커 */
    private String marker;
    private String markerSelected;
    private String markerId;
    private String posExact;
    private String itemLevel;

    /* 편의·예약 */
    private String menuExist;
    private Boolean hasNaverBooking;
    private Boolean hasNaverSmartOrder;
    private String coupon;
}

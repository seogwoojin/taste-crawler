package com.prography.restaurantscraper.kakaoReview.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActReview {
    private int event;
    private List<ScoreTitle> place_banner;
}

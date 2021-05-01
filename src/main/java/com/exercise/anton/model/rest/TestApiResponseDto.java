package com.exercise.anton.model.rest;

import com.exercise.anton.model.RiskEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestApiResponseDto {
    private double score;
    private String category;
    private RiskEnum risk;
}

package com.exercise.anton.model.rest;

import com.exercise.anton.model.RiskEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrichedRow {
    private double score;
    private String category;
    private RiskEnum risk;
    private String batch;
    private String id;
    private String client;
    private String title;
    private String description;
    private double price;
}

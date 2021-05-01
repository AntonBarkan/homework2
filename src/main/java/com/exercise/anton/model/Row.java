package com.exercise.anton.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Row {
    private String batch;
    private String id;
    private String client;
    private String title;
    private String description;
    private double price;
}

package com.example.project3.model;

import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(fluent = true)
public class CarModel {
    private final String id;

    @SerializedName("model")
    private final String value;
}

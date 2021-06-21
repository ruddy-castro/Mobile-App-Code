package com.example.project3.model;

import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(fluent = true)
public class Car {
    @SerializedName("vehicle_make_id")
    private final String makeId;

    @SerializedName("vehicle_model_id")
    private final String modelId;

    @SerializedName("vehicle_make")
    private final String make;

    @SerializedName("model")
    private final String model;
}

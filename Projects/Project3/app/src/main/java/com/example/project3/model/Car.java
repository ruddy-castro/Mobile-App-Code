package com.example.project3.model;

import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * A class that represents a Car object. This class uses Lombok to create the getters and setters.
 * Each variable is serialized based on the JSON attribute name from the given API.
 */
@Builder
@Data
@Accessors(fluent = true)
public class Car {
    @SerializedName("color")
    private final String color;

    @SerializedName("created_at")
    private final String createdAt;

    @SerializedName("id")
    private final String id;

    @SerializedName("image_url")
    private final String image_url;

    @SerializedName("mileage")
    private final String mileage;

    @SerializedName("model")
    private final String model;

    @SerializedName("price")
    private final Double price;

    @SerializedName("veh_description")
    private final String vehDescription;

    @SerializedName("vehicle_make")
    private final String vehicleMake;

    @SerializedName("vehicle_url")
    private final String vehicleUrl;

    @SerializedName("vin_number")
    private final String vinNumber;

    @SerializedName("updated_at")
    private final String lastUpdated;
}

package com.example.project3.model;

import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *  A class that represents a CarModel object. This class uses Lombok to create the getters and setters.
 *  Some variables are serialized based on the JSON attribute name from the given API.
 */
@Builder
@Data
@Accessors(fluent = true)
public class CarModel {
    private final String id;

    @SerializedName("model")
    private final String value;

    /**
     * Need to override to only display the model.
     * @return - returns the model of the car as a String.
     */
    @Override
    public String toString() {
        return value;
    }
}

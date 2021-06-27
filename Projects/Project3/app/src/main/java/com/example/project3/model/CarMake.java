package com.example.project3.model;

import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *  A class that represents a CarMake object. This class uses Lombok to create the getters and setters.
 *  Some variables are serialized based on the JSON attribute name from the given API.
 */
@Builder
@Data
@Accessors(fluent = true)
public class CarMake {
    private final String id;

    @SerializedName("vehicle_make")
    private final String value;

    /**
     * Need to override to only display the make.
     * @return - returns the make of the car as a String.
     */
    @Override
    public String toString() {
        return value;
    }
}

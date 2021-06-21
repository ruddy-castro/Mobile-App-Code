package com.example.project3.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(fluent = true)
public class CarMake {
    private final String id;
    private final String value;
}

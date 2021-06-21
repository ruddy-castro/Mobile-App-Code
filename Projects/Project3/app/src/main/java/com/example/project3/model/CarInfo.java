package com.example.project3.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(fluent = true)
public class CarInfo {
    private final CarMake make;
    private final CarModel model;
}

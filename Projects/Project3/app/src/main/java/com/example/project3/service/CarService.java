package com.example.project3.service;

import com.example.project3.model.Car;
import com.example.project3.model.CarMake;
import com.example.project3.model.CarModel;

import java.util.List;
import java.util.function.Consumer;

public interface CarService {
    void getAvailableCarMakes(Consumer<List<CarMake>> callback);
    void getAvailableCarModels(String makeId, Consumer<List<CarModel>> callback);
    void getAvailableCars(String makeId, String modelId, String zipCode, Consumer<List<Car>> callback);
    void getCarDetails(String carId, Consumer<Car> callback);
}

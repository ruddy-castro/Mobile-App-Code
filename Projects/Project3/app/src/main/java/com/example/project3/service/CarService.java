package com.example.project3.service;

import com.example.project3.model.Car;
import com.example.project3.model.CarMake;
import com.example.project3.model.CarModel;

import java.util.List;

public interface CarService {
    List<CarMake> getAvailableCarMakes();
    List<CarModel> getAvailableCarModels(String makeId);
    List<Car> getAvailableCars(String makeId, String modelId, String zipCode);
    Car getCarDetails(String carId);
}

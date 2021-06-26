package com.example.project3.service;

import com.example.project3.model.Car;
import com.example.project3.model.CarMake;
import com.example.project3.model.CarModel;

import java.util.List;
import java.util.function.Consumer;

/**
 * Interface for making API calls to get car related info
 */
public interface CarService {

    /**
     * Get a list of available car makes.
     *
     * @param callback the callback function
     */
    void getAvailableCarMakes(Consumer<List<CarMake>> callback);

    /**
     * Get a list of car models with the given car make id.
     *
     * @param makeId the car make id
     * @param callback the callback function
     */
    void getAvailableCarModels(String makeId, Consumer<List<CarModel>> callback);

    /**
     * Get a list of available cars given the car make id, car model id, and the zip code.
     *
     * @param makeId the car make id
     * @param modelId the car model id
     * @param zipCode the zip code
     * @param callback the callback function
     */
    void getAvailableCars(String makeId, String modelId, String zipCode, Consumer<List<Car>> callback);

    /**
     * Get the car details from the given car id.
     *
     * @param carId the car id
     * @param callback the callback function
     */
    void getCarDetails(String carId, Consumer<Car> callback);
}

package com.example.project3.service;

import android.util.Log;

import com.example.project3.model.Car;
import com.example.project3.model.CarMake;
import com.example.project3.model.CarModel;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Implementation of the CarService interface.
 */
public class CarServiceImpl implements CarService {

    // Object to serialize and deserialize POJO objects
    private final Gson GSON = new Gson();

    // Base url to construct the endpoint
    private final String BASE_URL = "https://thawing-beach-68207.herokuapp.com";

    // The tag for logging purpose
    private static final String TAG = CarServiceImpl.class.getSimpleName();

    // The executor to execute the API calls in another thread
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // The instance for Singleton pattern
    private static CarServiceImpl instance;

    /**
     * Private constructor for Singleton pattern.
     */
    private CarServiceImpl() { }

    /**
     * Get the singleton instance of this class.
     * @return the singleton instance of this class
     */
    public static CarService getInstance() {
        if (instance == null) {
            synchronized (CarServiceImpl.class) {
                if (instance == null) {
                    instance = new CarServiceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public void getAvailableCarMakes(Consumer<List<CarMake>> callback) {
        executor.execute(() -> {
            Log.i(TAG, "Get available car makes");

            // Construct the URL for the GET request
            final String reqUrl = String.format("%s/carmakes", BASE_URL);

            // Make the GET request and get the response string
            String response = makeGetRequest(reqUrl);
            Log.i(TAG, "Response string = " + response);

            // Deserialize the response to a list of car makes
            final List<CarMake> carMakes = GSON.fromJson(response, new TypeToken<List<CarMake>>() {}.getType());

            // Execute the callback method
            callback.accept(carMakes);
        });
    }

    @Override
    public void getAvailableCarModels(String makeId, Consumer<List<CarModel>> callback) {
        executor.execute(() -> {
            Log.i(TAG, "Get available car models");

            // Construct the URL for the GET request
            final String reqUrl = String.format("%s/carmodelmakes/%s", BASE_URL, makeId);

            // Make the GET request and get the response string
            String response = makeGetRequest(reqUrl);
            Log.i(TAG, "Response string = " + response);

            // Deserialize the response to a list of car models
            final List<CarModel> carModels = GSON.fromJson(response, new TypeToken<List<CarModel>>() {}.getType());

            // Execute the callback method
            callback.accept(carModels);
        });
    }

    @Override
    public void getAvailableCars(String makeId, String modelId, String zipCode, Consumer<List<Car>> callback) {
        executor.execute(() -> {
            Log.i(TAG, "Get available cars");

            // Construct the URL for the GET request
            final String reqUrl = String.format("%s/cars/%s/%s/%s", BASE_URL, makeId, modelId, zipCode);

            // Make the GET request and get the response string
            String response = makeGetRequest(reqUrl);
            Log.i(TAG, "Response string = " + response);

            // Skip the wrapper "list"
            JsonElement lists = JsonParser.parseString(response).getAsJsonObject().get("lists");
            Log.i(TAG, "lists = " + lists);

            // Deserialize the response to a list of cars
            final List<Car> cars = GSON.fromJson(lists, new TypeToken<List<Car>>() {}.getType());

            // Execute the callback method
            callback.accept(cars);
        });
    }

    @Override
    public void getCarDetails(String carId, Consumer<Car> callback) {
        executor.execute(() -> {
            Log.i(TAG, "Get car details");

            // Construct the URL for the GET request
            final String reqUrl = String.format("%s/cars/%s", BASE_URL, carId);

            // Make the GET request and get the response string
            String response = makeGetRequest(reqUrl);
            Log.i(TAG, "Response string = " + response);

            // Deserialize the response to a car object
            List<Car> cars = GSON.fromJson(response, new TypeToken<List<Car>>() { }.getType());
            final Car car = cars.isEmpty() ? null : cars.get(0);

            // Execute the callback method
            callback.accept(car);
        });
    }

    private String makeGetRequest(String reqUrl) {
        try {
            // Create the http connection
            final URL url = new URL(reqUrl);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Read the response into a single string
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                conn.setRequestMethod("GET");
                final StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                return sb.toString();
            } finally {
                conn.disconnect();
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage(), e);
            throw new CarServiceException("Unable to make GET request", e);
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage(), e);
            throw new CarServiceException("Unable to make GET request", e);
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage(), e);
            throw new CarServiceException("Unable to make GET request", e);
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage(), e);
            throw new CarServiceException("Unable to make GET request", e);
        }
    }
}

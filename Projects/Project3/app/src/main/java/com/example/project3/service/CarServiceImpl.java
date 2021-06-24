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

public class CarServiceImpl implements CarService {

    private final Gson GSON = new Gson();
    private final String BASE_URL = "https://thawing-beach-68207.herokuapp.com";
    private static final String TAG = CarServiceImpl.class.getSimpleName();

    private static CarServiceImpl instance;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private CarServiceImpl() { }

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
            final String reqUrl = String.format("%s/carmakes", BASE_URL);
            String response = makeGetRequest(reqUrl);
            Log.i(TAG, "Response string = " + response);
            final List<CarMake> carMakes = GSON.fromJson(response, new TypeToken<List<CarMake>>() {}.getType());
            callback.accept(carMakes);
        });
    }

    @Override
    public void getAvailableCarModels(String makeId, Consumer<List<CarModel>> callback) {
        executor.execute(() -> {
            Log.i(TAG, "Get available car models");
            final String reqUrl = String.format("%s/carmodelmakes/%s", BASE_URL, makeId);
            String response = makeGetRequest(reqUrl);
            Log.i(TAG, "Response string = " + response);
            final List<CarModel> carModels = GSON.fromJson(response, new TypeToken<List<CarModel>>() {}.getType());
            callback.accept(carModels);
        });
    }

    @Override
    public void getAvailableCars(String makeId, String modelId, String zipCode, Consumer<List<Car>> callback) {
        executor.execute(() -> {
            Log.i(TAG, "Get available cars");
            final String reqUrl = String.format("%s/cars/%s/%s/%s", BASE_URL, makeId, modelId, zipCode);
            String response = makeGetRequest(reqUrl);
            Log.i(TAG, "Response string = " + response);

            // Skip the wrapper "list"
            JsonElement lists = JsonParser.parseString(response).getAsJsonObject().get("lists");
            Log.i(TAG, "lists = " + lists);

            final List<Car> cars = GSON.fromJson(lists, new TypeToken<List<Car>>() {}.getType());
            callback.accept(cars);
        });
    }

    @Override
    public void getCarDetails(String carId, Consumer<Car> callback) {
        executor.execute(() -> {
            Log.i(TAG, "Get car details");
            final String reqUrl = String.format("%s/cars/%s", BASE_URL, carId);
            String response = makeGetRequest(reqUrl);
            Log.i(TAG, "Response string = " + response);
            List<Car> cars = GSON.fromJson(response, new TypeToken<List<Car>>() {}.getType());
            final Car car = cars.isEmpty() ? null : cars.get(0);
            callback.accept(car);
        });
    }

    private String makeGetRequest(String reqUrl) {
        try {
            final URL url = new URL(reqUrl);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

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

public class CarServiceImpl implements CarService {

    private final Gson GSON = new Gson();
    private final String BASE_URL = "https://thawing-beach-68207.herokuapp.com";
    private static final String TAG = CarServiceImpl.class.getSimpleName();

    private static CarServiceImpl instance;

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
    public List<CarMake> getAvailableCarMakes() {
        Log.i(TAG, "Get available car makes");
        final String reqUrl = String.format("%s/carmakes", BASE_URL);
        String response = makeGetRequest(reqUrl);
        Log.i(TAG, "Response string = " + response);
        return GSON.fromJson(response, new TypeToken<List<CarMake>>() {}.getType());
    }

    @Override
    public List<CarModel> getAvailableCarModels(String makeId) {
        Log.i(TAG, "Get available car models");
        final String reqUrl = String.format("%s/carmodelmakes/%s", BASE_URL, makeId);
        String response = makeGetRequest(reqUrl);
        Log.i(TAG, "Response string = " + response);
        return GSON.fromJson(response, new TypeToken<List<CarModel>>() {}.getType());
    }

    @Override
    public List<Car> getAvailableCars(String makeId, String modelId, String zipCode) {
        Log.i(TAG, "Get available cars");
        final String reqUrl = String.format("%s/cars/%s/%s/%s", BASE_URL, makeId, modelId, zipCode);
        String response = makeGetRequest(reqUrl);
        Log.i(TAG, "Response string = " + response);

        // Skip the wrapper "list"
        JsonElement lists = JsonParser.parseString(response).getAsJsonObject().get("lists");
        Log.i(TAG, "lists = " + lists);

        return GSON.fromJson(lists, new TypeToken<List<Car>>() {}.getType());
    }

    @Override
    public Car getCarDetails(String carId) {
        Log.i(TAG, "Get car details");
        final String reqUrl = String.format("%s/cars/%s", BASE_URL, carId);
        String response = makeGetRequest(reqUrl);
        Log.i(TAG, "Response string = " + response);
        List<Car> cars = GSON.fromJson(response, new TypeToken<List<Car>>() {}.getType());
        return cars.isEmpty() ? null : cars.get(0);
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
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage(), e);
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage(), e);
        }

        return null;
    }
}

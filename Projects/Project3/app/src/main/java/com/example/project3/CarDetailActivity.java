package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.project3.model.Car;

import java.util.ArrayList;
import java.util.HashMap;

public class CarDetailActivity extends AppCompatActivity {
    HashMap<String, String> details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        details = new HashMap<>();

        details.put("make", getIntent().getStringExtra("make"));
        details.put("model", getIntent().getStringExtra("model"));
        details.put("price", getIntent().getStringExtra("price"));
        details.put("description", getIntent().getStringExtra("description"));
        details.put("imageURL", getIntent().getStringExtra("image"));
    }
}
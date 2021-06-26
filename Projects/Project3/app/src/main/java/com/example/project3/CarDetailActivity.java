package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project3.service.CarService;
import com.example.project3.service.CarServiceImpl;
import com.squareup.picasso.Picasso;

public class CarDetailActivity extends AppCompatActivity {
    TextView mMakeModel;
    TextView mPrice;
    TextView mDetails;
    TextView mLastUpdate;
    ImageView mCarImage;

    private CarService carService = CarServiceImpl.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        mMakeModel = findViewById(R.id.make_model);
        mPrice = findViewById(R.id.price);
        mDetails = findViewById(R.id.car_detail);
        mLastUpdate = findViewById(R.id.last_update);
        mCarImage = findViewById(R.id.car_image);

        // Extract car details from the intent
        final String carMake = getIntent().getStringExtra("make");
        final String carModel = getIntent().getStringExtra("model");
        final String carId = getIntent().getStringExtra("id");

        // Update the GUI with car details
        carService.getCarDetails(carId, (car) -> {
            runOnUiThread(() -> {
                mMakeModel.setText(String.format("%s %s", carMake, carModel));
                mPrice.setText(String.format("$%.2f", car.price()));
                mDetails.setText(car.vehDescription());
                mLastUpdate.setText(String.format("Last Updated: %s", car.lastUpdated()));

                // Get image, if available, and set image view with it
                Picasso.get().load(car.image_url()).into(mCarImage);

                // if no image available
                if (mCarImage.getDrawable() == null)
                    Picasso.get().load("https://www.car-info.com/build/images/no_img.jpg?v2.2").into(mCarImage);
            });
        });
    }
}
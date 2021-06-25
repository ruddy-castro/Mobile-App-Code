package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project3.model.Car;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class CarDetailActivity extends AppCompatActivity {
    HashMap<String, String> details;
    TextView mMakeModel;
    TextView mPrice;
    TextView mDetails;
    TextView mLastUpdate;
    ImageView mCarImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        details = new HashMap<>();
        mMakeModel = findViewById(R.id.make_model);
        mPrice = findViewById(R.id.price);
        mDetails = findViewById(R.id.car_detail);
        mLastUpdate = findViewById(R.id.last_update);
        mCarImage = findViewById(R.id.car_image);

        details.put("make", getIntent().getStringExtra("make"));
        details.put("model", getIntent().getStringExtra("model"));
        details.put("price", getIntent().getStringExtra("price"));
        details.put("description", getIntent().getStringExtra("description"));
        details.put("imageURL", getIntent().getStringExtra("image"));
        details.put("lastUpdated", getIntent().getStringExtra("lastUpdated"));

        mMakeModel.setText(details.get("make") + " " + details.get("model"));
        mPrice.setText("$" + details.get("price") + "0");
        mDetails.setText(details.get("description"));
        mLastUpdate.setText(details.get("lastUpdated"));

        // Get image, if available, and set image view with it
        Picasso.get().load(details.get("imageURL")).into(mCarImage);

        // if no image available
        if (mCarImage.getDrawable() == null)
            Picasso.get().load("https://www.car-info.com/build/images/no_img.jpg?v2.2").into(mCarImage);



    }
}
package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.project3.model.Car;
import com.example.project3.model.CarMake;
import com.example.project3.model.CarModel;
import com.example.project3.service.CarService;
import com.example.project3.service.CarServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity{
    private boolean mTwoPane = false;
    final String TAG = MainActivity.class.getSimpleName();

    ArrayList<HashMap<String, String>> makeList;
    ArrayList<String> modelList;
    private RecyclerView rv;
    private CarService carService = CarServiceImpl.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);

        makeList = new ArrayList<>();
        modelList = new ArrayList<>();
        rv = findViewById(R.id.available_vehicle);

        // Initialize spinner for car makes and car models
        final Spinner carMakesSpinner = findViewById(R.id.available_cars);
        final Spinner carModelsSpinner = findViewById(R.id.available_models);

        carMakesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Extract the selected car make
                final CarMake carMake = (CarMake) parent.getItemAtPosition(position);
                Log.i(TAG, "Ly: selected carMake = " + carMake);

                // Setup the car models spinner
                carService.getAvailableCarModels(carMake.id(), (carModels) -> {
                    Log.i(TAG, "Ly: car models = " + carModels);
                    setUpSpinnerAdapter(carModelsSpinner, carModels);

                    carModelsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                        {
                            final CarModel carModel = (CarModel) parent.getItemAtPosition(position);
                            Log.i(TAG, "Ruddy: selected carModel = " + carModel);

                            carService.getAvailableCars(carMake.id(), carModel.id(), "92603", (availableCars) -> {
                                Log.i(TAG, "Ruddy: available cars = " + availableCars);

                                SimpleItemRecyclerViewAdapter ra = new SimpleItemRecyclerViewAdapter(availableCars);

                                runOnUiThread(() -> {
                                    rv.setAdapter(ra);});
                            });
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Make a call to get available car makes to setup car make spinner
        carService.getAvailableCarMakes((carMakes) -> {
            Log.i(TAG, "Ly: car makes = " + carMakes);
            setUpSpinnerAdapter(carMakesSpinner, carMakes);
        });

        // is the container layout available? if so, set mTwoPane to true.
        if (findViewById(R.id.car_detail_container) != null) {
            mTwoPane = true;
        }
    }


    private <T> void setUpSpinnerAdapter(Spinner s, List<T> data)
    {
        ArrayAdapter<T> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // This is to prevent "CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views."
        runOnUiThread(() -> {
            s.setAdapter(aa);
        });
    }


    // RecyclerView Adapter Class
    class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter <SimpleItemRecyclerViewAdapter.ViewHolder>
    {
        private final List<Car> mCars;

        SimpleItemRecyclerViewAdapter(List<Car> items)
        {
            mCars = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder (final ViewHolder holder, int position)
        {
            holder.mItem = mCars.get(position);
            holder.mIdView.setText(String.valueOf(position + 1));
            holder.mMakeView.setText(mCars.get(position).vehicleMake());
            holder.mPriceView.setText("$" + mCars.get(position).price() + "0");


            holder.mView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        int selectedCar = holder.getAdapterPosition();

                        CarDetailFragment frg = CarDetailFragment.newInstance(mCars.get(selectedCar), "");
                        getSupportFragmentManager().beginTransaction().replace(R.id.car_detail_container, frg)
                                .addToBackStack(null).commit();
                    }
                    else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, CarDetailActivity.class);

                        carService.getCarDetails(mCars.get(position).id(), (carDetails) -> {
                            Log.i(TAG, "Ruddy: car details = " + carDetails);
                            intent.putExtra("lastUpdated", carDetails.lastUpdated());
                        });

                        // TODO: Figure out how to send a Car object instead
                        // Doing each string individually for now.
                        intent.putExtra("id", mCars.get(holder.getAdapterPosition()).id());
                        intent.putExtra("make", mCars.get(holder.getAdapterPosition()).vehicleMake());
                        intent.putExtra("model", mCars.get(holder.getAdapterPosition()).model());
                        intent.putExtra("price", mCars.get(holder.getAdapterPosition()).price());
                        intent.putExtra("description", mCars.get(holder.getAdapterPosition()).vehDescription());
                        intent.putExtra("image", mCars.get(holder.getAdapterPosition()).image_url());

                        // TODO: Get the last updated from the detailed API

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() { return mCars.size(); }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            final View mView;
            final TextView mIdView;
            final TextView mMakeView;
            final TextView mPriceView;
            Car mItem;

            ViewHolder(View itemView)
            {
                super(itemView);
                mView = itemView;

                mIdView = itemView.findViewById(R.id.id);
                mMakeView = itemView.findViewById(R.id.tvMake);
                mPriceView = itemView.findViewById(R.id.tvPrice);
            }
        }
    }
}
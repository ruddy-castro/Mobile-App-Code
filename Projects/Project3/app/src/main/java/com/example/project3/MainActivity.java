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

/**
 * Main class that handles the application logic by calling the various classes and their methods.
 */
public class MainActivity extends AppCompatActivity {
    // set twoPane mode to false
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

        // Setting up listener for the Car make Spinner
        carMakesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Extract the selected car make
                final CarMake carMake = (CarMake) parent.getItemAtPosition(position);
                Log.i(TAG, "selected carMake = " + carMake);

                // Setup the car models spinner
                carService.getAvailableCarModels(carMake.id(), (carModels) -> {
                    Log.i(TAG, "car models = " + carModels);
                    setUpSpinnerAdapter(carModelsSpinner, carModels);

                    // Setting up listener for the Car model Spinner
                    carModelsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            final CarModel carModel = (CarModel) parent.getItemAtPosition(position);
                            Log.i(TAG, "selected carModel = " + carModel);

                            // using CarService to make the API call to get the available cars
                            // availableCars is the callBack (this code will run after the function is completed)
                            carService.getAvailableCars(carMake.id(), carModel.id(), "92603", (availableCars) -> {
                                Log.i(TAG, "available cars = " + availableCars);

                                SimpleItemRecyclerViewAdapter ra = new SimpleItemRecyclerViewAdapter(availableCars);

                                // This is needed since we want to update the UI from a Non-UI thread
                                // Non-UI thread since we are in the setOnItemSelectedListener scope
                                runOnUiThread(() -> {
                                    rv.setAdapter(ra);
                                });
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Make a call to get available car makes to setup car make spinner
        // carMakes is the callBack (this code will run after the function is completed)
        carService.getAvailableCarMakes((carMakes) -> {
            Log.i(TAG, "car makes = " + carMakes);
            setUpSpinnerAdapter(carMakesSpinner, carMakes);
        });

        // is the container layout available? if so, set mTwoPane to true.
        if (findViewById(R.id.car_detail_container) != null) {
            mTwoPane = true;
        }
    }

    /**
     * This templated function takes the data and displays it to the designated spinner.
     * @param s - spinner that will display data
     * @param data - list of information that will be displayed
     * @param <T> - template for the type of data
     */
    private <T> void setUpSpinnerAdapter(Spinner s, List<T> data) {
        ArrayAdapter<T> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // This is to prevent "CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views."
        runOnUiThread(() -> {
            s.setAdapter(aa);
        });
    }


    // RecyclerView Adapter Class
    class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private final List<Car> mCars;

        SimpleItemRecyclerViewAdapter(List<Car> items) {
            mCars = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mCars.get(position);
            holder.mIdView.setText(String.valueOf(position + 1));
            holder.mMakeView.setText(mCars.get(position).vehicleMake());
            holder.mPriceView.setText("$" + mCars.get(position).price() + "0");


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Car selectedCar = mCars.get(holder.getAdapterPosition());

                        CarDetailFragment frg = CarDetailFragment.newInstance(selectedCar);
                        getSupportFragmentManager().beginTransaction().replace(R.id.car_detail_container, frg)
                                .addToBackStack(null).commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, CarDetailActivity.class);

                        intent.putExtra("id", mCars.get(holder.getAdapterPosition()).id());
                        intent.putExtra("make", mCars.get(holder.getAdapterPosition()).vehicleMake());
                        intent.putExtra("model", mCars.get(holder.getAdapterPosition()).model());

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mCars.size();
        }

        // ViewHolder class for the RecyclerView Adapter
        class ViewHolder extends RecyclerView.ViewHolder {
            final View mView;
            final TextView mIdView;
            final TextView mMakeView;
            final TextView mPriceView;
            Car mItem;

            ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;

                mIdView = itemView.findViewById(R.id.id);
                mMakeView = itemView.findViewById(R.id.tvMake);
                mPriceView = itemView.findViewById(R.id.tvPrice);
            }
        }
    }
}
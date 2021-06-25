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
import android.widget.Toast;

import com.example.project3.model.Car;
import com.example.project3.model.CarMake;
import com.example.project3.model.CarModel;
import com.example.project3.service.CarService;
import com.example.project3.service.CarServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    // URLS
    private static String makesURL = "https://thawing-beach-68207.herokuapp.com/carmakes";
    private static String modelsURL = "https://thawing-beach-68207.herokuapp.com/carmodelmakes";
    // Search URL requires more info:
    //      Available cars: .../<make>/<model>/<zipcode>
    //      Vehicle Details: .../<carid>
    private static String searchURL = "https://thawing-beach-68207.herokuapp.com/cars/";

    final String TAG = MainActivity.class.getSimpleName();


    ArrayList<HashMap<String, String>> makeList;
    //ArrayList<HashMap<String, String>> modelList;
    ArrayList<String> modelList;
    private RecyclerView rv;
    private String selectedMake;

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

        carMakesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Extract the selected car make
                final CarMake carMake = (CarMake) parent.getItemAtPosition(position);
                Log.i(TAG, "Ly: selected carMake = " + carMake);

                // Setup the car models spinner
                carService.getAvailableCarModels(carMake.id(), (carModels) -> {
                    Log.i(TAG, "Ly: car models = " + carModels);
                    setUpSpinnerAdapter(carModelsSpinner, carModels);

                    carModelsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
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
    }

//    /**
//     * Class to get the makes of the vehicles using the URL
//     */
//    @SuppressWarnings("deprecation")
//    private class getMakes extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            System.out.println("Ly: get makes");
//            System.out.println(carService.getAvailableCarMakes());
//
//            // Transfer car makes to array list
//            List<CarMake> ml = carService.getAvailableCarMakes();
//            for(int i = 0; i < ml.size(); i++)
//            {
//                HashMap<String, String> make = new HashMap<>();
//
//                make.put("id", ml.get(i).id());
//                make.put("vehicle_make", ml.get(i).value());
//
//                makeList.add(make);
//            }
//
////            HttpHandler sh = new HttpHandler();
////
////            // Added beginning and end strings to turn string into JSONObject
////            String jsonStr = "{\"makes\": " + sh.makeServiceCall(makesURL) + "}";
////
////            if (jsonStr != null) {
////                try {
////                    JSONObject jsonObj = new JSONObject(jsonStr);
////
////                    JSONArray carMakes = jsonObj.getJSONArray("makes");
////
////                    for (int i = 0; i < carMakes.length(); i++) {
////                        JSONObject c = carMakes.getJSONObject(i);
////
////                        String id = c.getString("id");
////                        String make = c.getString("vehicle_make");
////
////                        HashMap<String, String> carMake = new HashMap<>();
////                        carMake.put("id", id);
////                        carMake.put("make", make);
////
////                        makeList.add(carMake);
////                    }
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
////            }
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            // TODO: Display list
//            super.onPostExecute(result);
//
//            // populate make Spinner
//            Spinner makes = findViewById(R.id.available_cars);
//            //makes.setOnItemSelectedListener(this);
//
//            // Update the model spinner
//            makes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<CarMake> parent, View view, int position, long id) {
//                    parent.getItemAtPosition(0);
//                    selectedMake = makeList.get(position).get("id");
//                    Toast.makeText(getApplicationContext(), selectedMake, Toast.LENGTH_SHORT).show();
//
//                    // populate model Spinner
//                    Spinner models = findViewById(R.id.available_models);
//                    //models.setOnItemSelectedListener(this);
//
//                    setUpAdapter(models, modelList);
//
//                    //new getModels().execute();
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//
//            // Test: Hardcoded array for now
//            // TODO: Get parsed data to be used instead
//            ArrayList<String> m = new ArrayList<>(Arrays.asList("Aston Martin", "Bentley", "BMW","Bugatti","Ferrari","Jaguar","Lamborghini","Maserati","Porsche","Tesla"));
//
//            setUpAdapter(makes, m);
//        }
//    }
//
    private <T> void setUpSpinnerAdapter(Spinner s, List<T> data) {
        ArrayAdapter<T> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // This is to prevent "CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views."
        runOnUiThread(() -> {
            s.setAdapter(aa);
        });
    }
//
//    /**
//     * Class to get the models of the vehicles using the URL
//     */
//    @SuppressWarnings("deprecation")
//    private class getModels extends AsyncTask<Void, Void, Void> implements AdapterView.OnItemSelectedListener{
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            System.out.println("Ly: get models");
//            System.out.println(carService.getAvailableCarModels(selectedMake));
//
//            // Transfer car makes to array list
//            List<CarModel> cm = carService.getAvailableCarModels(selectedMake);
//
//            modelList.clear();
//
//            for(int i = 0; i < cm.size(); i++)
//                modelList.add(cm.get(i).id());
//
//
//
//
////            System.out.println("Ly: get cars");
////            System.out.println(carService.getAvailableCars("10", "20", "92603"));
////            System.out.println("Ly: get details");
////            System.out.println(carService.getCarDetails("3484"));
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//        }
//
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//
//        }
//    }

    // RecyclerView Adapter Class
    class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter <SimpleItemRecyclerViewAdapter.ViewHolder>
    {
        private final List<Car> mCars;

        SimpleItemRecyclerViewAdapter(List<Car> items)
        {
            mCars = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder (final ViewHolder holder, int position) {
            holder.mItem = mCars.get(position);
            holder.mIdView.setText(String.valueOf(position + 1));
            holder.mMakeView.setText(mCars.get(position).vehicleMake());
            holder.mPriceView.setText("$" + mCars.get(position).price() + "0");

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, CarDetailActivity.class);

                    // TODO: Figure out how to send a Car object instead
                    // Doing each string individually for now.
                    intent.putExtra("make", mCars.get(holder.getAdapterPosition()).vehicleMake());
                    intent.putExtra("model", mCars.get(holder.getAdapterPosition()).model());
                    intent.putExtra("price", mCars.get(holder.getAdapterPosition()).price());
                    intent.putExtra("description", mCars.get(holder.getAdapterPosition()).vehDescription());
                    intent.putExtra("image", mCars.get(holder.getAdapterPosition()).image_url());

                    // TODO: Get the last updated from the detailed API
                    // intent.putExtra("lastUpdate", mCars.get(holder.getAdapterPosition()).createdAt());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mCars.size();
        }

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
package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.project3.model.CarMake;
import com.example.project3.model.CarModel;
import com.example.project3.service.CarService;
import com.example.project3.service.CarServiceImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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
    private ListView lv;
    private String selectedMake;

    private CarService carService = CarServiceImpl.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);

        makeList = new ArrayList<>();
        modelList = new ArrayList<>();
        lv = findViewById(R.id.list);

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
                    setUpAdapter(carModelsSpinner, carModels);
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Make a call to get available car makes to setup car make spinner
        carService.getAvailableCarMakes((carMakes) -> {
            Log.i(TAG, "Ly: car makes = " + carMakes);
            setUpAdapter(carMakesSpinner, carMakes);
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
    private <T> void setUpAdapter(Spinner s, List<T> data) {
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


}
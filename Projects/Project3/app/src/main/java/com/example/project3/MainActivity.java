package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.example.project3.model.CarMake;
import com.example.project3.model.CarModel;
import com.example.project3.service.CarService;
import com.example.project3.service.CarServiceImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    // URLS
    private static String makesURL = "https://thawing-beach-68207.herokuapp.com/carmakes";
    private static String modelsURL = "https://thawing-beach-68207.herokuapp.com/carmodelmakes";
    // Search URL requires more info:
    //      Available cars: .../<make>/<model>/<zipcode>
    //      Vehicle Details: .../<carid>
    private static String searchURL = "https://thawing-beach-68207.herokuapp.com/cars/";


    ArrayList<HashMap<String, String>> makeList;
    ArrayList<HashMap<String, String>> modelList;
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


        new getMakes().execute();

        // populate make Spinner
        Spinner makes = findViewById(R.id.available_cars);
        makes.setOnItemSelectedListener(this);

        ArrayList<String> m = new ArrayList<>();
        //m.add("test");
        for(int i = 0; i < makeList.size(); i++)
            m.add(makeList.get(i).get("vehicle_make"));

        ArrayAdapter<String> aa = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, m);
        makes.setAdapter(aa);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Class to get the makes of the vehicles using the URL
     */
    @SuppressWarnings("deprecation")
    private class getMakes extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            System.out.println("Ly: get makes");
            System.out.println(carService.getAvailableCarMakes());

            // Transfer car makes to array list
            List<CarMake> ml = carService.getAvailableCarMakes();
            for(int i = 0; i < ml.size(); i++)
            {
                HashMap<String, String> make = new HashMap<>();

                make.put("id", ml.get(i).id());
                make.put("vehicle_make", ml.get(i).value());

                makeList.add(make);
            }

            System.out.println("Ly: get models");
            System.out.println(carService.getAvailableCarModels("10"));

            // Transfer car makes to array list
            List<CarModel> cm = carService.getAvailableCarModels("10");
            for(int i = 0; i < cm.size(); i++)
            {
                HashMap<String, String> model = new HashMap<>();

                model.put("vehicle_make_id", cm.get(i).id());
                model.put("model", cm.get(i).value());

                modelList.add(model);
            }

            System.out.println("Ly: get cars");
            System.out.println(carService.getAvailableCars("10", "20", "92603"));
            System.out.println("Ly: get details");
            System.out.println(carService.getCarDetails("3484"));

//            HttpHandler sh = new HttpHandler();
//
//            // Added beginning and end strings to turn string into JSONObject
//            String jsonStr = "{\"makes\": " + sh.makeServiceCall(makesURL) + "}";
//
//            if (jsonStr != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//
//                    JSONArray carMakes = jsonObj.getJSONArray("makes");
//
//                    for (int i = 0; i < carMakes.length(); i++) {
//                        JSONObject c = carMakes.getJSONObject(i);
//
//                        String id = c.getString("id");
//                        String make = c.getString("vehicle_make");
//
//                        HashMap<String, String> carMake = new HashMap<>();
//                        carMake.put("id", id);
//                        carMake.put("make", make);
//
//                        makeList.add(carMake);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO: Display list
            super.onPostExecute(result);
        }
    }

//    /**
//     * Class to get the models of the vehicles using the URL
//     */
//    @SuppressWarnings("deprecation")
//    private class getModels extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            HttpHandler sh = new HttpHandler();
//
//            // TODO: get makeID to reflect the chosen car make
//            String makeID = "10";
//
//            // Added beginning and end strings to turn string into JSONObject
//            String jsonStr = "{\"models\": " + sh.makeServiceCall(modelsURL + "/" + makeID) + "}";
//
//            if (jsonStr != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//
//                    JSONArray carModels = jsonObj.getJSONArray("models");
//
//                    for (int i = 0; i < carModels.length(); i++) {
//                        JSONObject m = carModels.getJSONObject(i);
//
//                        String modelID = m.getString("id");
//                        String modelNum = m.getString("model");
//
//                        HashMap<String, String> carMake = new HashMap<>();
//                        carMake.put("model_ID", modelID);
//                        carMake.put("model_number", modelNum);
//
//                        modelList.add(carMake);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            // TODO: Display list
//            super.onPostExecute(result);
//        }
//    }
}
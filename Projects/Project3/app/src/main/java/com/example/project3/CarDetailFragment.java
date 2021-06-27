package com.example.project3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project3.model.Car;
import com.example.project3.service.CarService;
import com.example.project3.service.CarServiceImpl;
import com.squareup.picasso.Picasso;

/**
 * A class to display car details in twopane.
 * A simple {@link Fragment} subclass.
 * Use the {@link CarDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarDetailFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "id";
    private static final String ARG_MAKE = "make";
    private static final String ARG_MODEL = "model";

    private String mId;
    private String mMake;
    private String mModel;

    private CarService carService = CarServiceImpl.getInstance();

    public CarDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param selectedCar The car to be displayed
     * @return the fragment
     */
    public static CarDetailFragment newInstance(Car selectedCar) {
        CarDetailFragment frg = new CarDetailFragment();
        Bundle arguments = new Bundle();

        arguments.putString(ARG_ID, selectedCar.id());
        arguments.putString(ARG_MAKE, selectedCar.vehicleMake());
        arguments.putString(ARG_MODEL, selectedCar.model());

        frg.setArguments(arguments);
        return frg;
    }

    /**
     * Initialize components for fragment
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getString(ARG_ID);
            mMake = getArguments().getString(ARG_MAKE);
            mModel = getArguments().getString(ARG_MODEL);
        }
    }

    /**
     * Provides the layout for the fragment
     * @param inflater to inflate the layout for this fragment
     * @param container the ViewGroup
     * @param savedInstanceState
     * @return a View, the root of the fragment's layout
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.car_detail, container, false);

        // Show the detail info in a TextView
        // Update the GUI with car details
        carService.getCarDetails(mId, (car) -> {
            getActivity().runOnUiThread(() -> {
                ((TextView) root.findViewById(R.id.make_model)).setText(mMake + " " + mModel);
                ((TextView) root.findViewById(R.id.price)).setText(String.format("$%.2f", car.price()));
                ((TextView) root.findViewById(R.id.car_detail)).setText(car.vehDescription());
                ((TextView) root.findViewById(R.id.last_update)).setText(String.format("Last Updated: %s", car.lastUpdated()));

                // Get image, if available, and set image view with it
                ImageView iv = root.findViewById(R.id.car_image);
                Picasso.get().load(car.image_url()).into(iv);

                // if no image available
                if (iv.getDrawable() == null)
                    Picasso.get().load("https://www.car-info.com/build/images/no_img.jpg?v2.2").into(iv);

            });
        });
        return root;
    }
}
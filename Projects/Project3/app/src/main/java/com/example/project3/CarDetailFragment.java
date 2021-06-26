package com.example.project3;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project3.model.Car;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarDetailFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "id";
    private static final String ARG_MAKE = "make";
    private static final String ARG_MODEL = "model";
    private static final String ARG_PRICE = "price";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_IMAGE = "imageURL";
    private static final String ARG_LAST_UPDATED = "lastUpdated";

    private String mId;
    private String mMake;
    private String mModel;
    private String mPrice;
    private String mDetails;
    private String mCarImage;
    private String mLastUpdate;

    public CarDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static CarDetailFragment newInstance(Car selectedCar) {
        CarDetailFragment frg = new CarDetailFragment();
        Bundle arguments = new Bundle();

        arguments.putString(ARG_ID, selectedCar.id());
        arguments.putString(ARG_MAKE, selectedCar.vehicleMake());
        arguments.putString(ARG_MODEL, selectedCar.model());
        arguments.putString(ARG_PRICE, selectedCar.price());
        arguments.putString(ARG_DESCRIPTION, selectedCar.vehDescription());
        arguments.putString(ARG_IMAGE, selectedCar.image_url());
        arguments.putString(ARG_LAST_UPDATED, selectedCar.lastUpdated());

        frg.setArguments(arguments);
        return frg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getString(ARG_ID);
            mMake = getArguments().getString(ARG_MAKE);
            mModel = getArguments().getString(ARG_MODEL);
            mPrice = getArguments().getString(ARG_PRICE);
            mDetails = getArguments().getString(ARG_DESCRIPTION);
            mCarImage = getArguments().getString(ARG_IMAGE);
            mLastUpdate = getArguments().getString(ARG_LAST_UPDATED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.car_detail, container, false);
        //shows the detail info in a TextView
        if (!mId.isEmpty()) {
            ((TextView) root.findViewById(R.id.make_model)).setText(mMake + " " + mModel);
            ((TextView) root.findViewById(R.id.price)).setText("$" + mPrice + "0");
            ((TextView) root.findViewById(R.id.car_detail)).setText(mDetails);
            ((TextView) root.findViewById(R.id.last_update)).setText("Last Updated: " + mLastUpdate);

            // Get image, if available, and set image view with it
            ImageView iv = root.findViewById(R.id.car_image);
            Picasso.get().load(mCarImage).into(iv);

            // if no image available
            if (iv.getDrawable() == null)
                Picasso.get().load("https://www.car-info.com/build/images/no_img.jpg?v2.2").into(iv);
        }
        return root;
    }
}
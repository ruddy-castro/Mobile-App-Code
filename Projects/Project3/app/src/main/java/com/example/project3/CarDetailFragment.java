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

    public HashMap<String, String> details;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Car mParam1;
    private String mParam2;

    public CarDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarDetailFragment newInstance(Car param1, String param2) {
        CarDetailFragment fragment = new CarDetailFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        args.putString("id", param1.id());
        args.putString("make", param1.vehicleMake());
        args.putString("model", param1.model());
        args.putString("price", param1.price());
        args.putString("description", param1.vehDescription());
        args.putString("imageURL", param1.image_url());
        args.putString("lastUpdated", param1.lastUpdated());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        details = new HashMap<>();
        if (getArguments() != null) {
            details.put("make", getArguments().getString("make"));
            details.put("model", getArguments().getString("model"));
            details.put("price", getArguments().getString("price"));
            details.put("description", getArguments().getString("description"));
            details.put("imageURL", getArguments().getString("image"));
            details.put("lastUpdated", getArguments().getString("lastUpdated"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.car_detail, container, false);
        //shows the detail info in a TextView
        if (details != null){
            //((TextView) root.findViewById (R.id.song_detail)).setText(mSong.details);
            ((TextView) root.findViewById(R.id.make_model)).setText(details.get("make") + " " + details.get("model"));
            ((TextView) root.findViewById(R.id.price)).setText("$" + details.get("price") + "0");
            ((TextView) root.findViewById(R.id.car_detail)).setText(details.get("description"));
            ((TextView) root.findViewById(R.id.last_update)).setText(details.get("lastUpdated"));

            // Get image, if available, and set image view with it
            ImageView iv = root.findViewById(R.id.car_image);
            Picasso.get().load(details.get("imageURL")).into(iv);

            // if no image available
            if (iv.getDrawable() == null)
                Picasso.get().load("https://www.car-info.com/build/images/no_img.jpg?v2.2").into(iv);
        }
        return root;
    }

    public static CarDetailFragment newInstance(int selectedCar){
        CarDetailFragment frg = new CarDetailFragment();
        Bundle arguments = new Bundle();



        return frg;

    }
}
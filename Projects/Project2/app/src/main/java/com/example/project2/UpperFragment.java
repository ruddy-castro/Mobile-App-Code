package com.example.project2;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class UpperFragment extends Fragment {

    private SharedData sharedData;

    /**
     * Initial creation of the fragment, non graphical initializations
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflate the layout of the fragment, graphical initializations.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_upper_fragment, container, false);
        ImageView image = (ImageView) root.findViewById(R.id.ivSingleImage);

        // Init shared data variable
        sharedData = new ViewModelProvider(requireActivity()).get(SharedData.class);
        sharedData.getSelectedItem().observe(getViewLifecycleOwner(), item -> {
            image.setImageResource(item);
        });


        return root;
    }


}
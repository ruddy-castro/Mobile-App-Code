package com.example.project2;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class LowerFragment extends Fragment {
    // The fragment initialization needed parameters
    private CheckBox chkGallery;
    private CheckBox chkSlide;
    private Button btnPrevious;
    private Button btnNext;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "numberOfImages";

    private int numberOfImages;

    private SharedData sharedData;

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture scheduledFuture;


    /**
     * Initial creation of the fragment, non graphical initializations
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            numberOfImages = getArguments().getInt(ARG_PARAM1);
        }
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
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_lower_fragment, container, false);

        // Wire up
        chkGallery = root.findViewById(R.id.cbGallery);
        chkSlide = root.findViewById(R.id.cbSlideShow);
        btnPrevious = root.findViewById(R.id.btnPrevious);
        btnNext = root.findViewById(R.id.btnNext);

        // Init shared data variable
        sharedData = new ViewModelProvider(requireActivity()).get(SharedData.class);

        // Observe the selected index
        sharedData.getSelectedItemIndex().observe(getViewLifecycleOwner(), index -> {
            log("selected index change to " + index);
            btnPrevious.setEnabled(index != 0);
            btnNext.setEnabled(index != numberOfImages - 1);
        });

        // Add listener for the back button
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Decrement selected index
                log("previous button clicked");
                sharedData.setSelectedItemIndex(sharedData.getSelectedItemIndex().getValue() - 1);
            }
        });

        // Add listener for the next button
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment selected index
                log("next button clicked");
                sharedData.setSelectedItemIndex(sharedData.getSelectedItemIndex().getValue() + 1);
            }
        });


        // Add listener for the checkbox slideshow button
        chkSlide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                log("next button clicked");
                if (isChecked) {
                    log("start slideshow");
                    scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
                        try {
                            log("next picture");
                            int nextIndex = (sharedData.getSelectedItemIndex().getValue() + 1) % numberOfImages;
                            sharedData.setSelectedItemIndex(nextIndex);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, 3, 3, TimeUnit.SECONDS);
                } else {
                    log("cancel slideshow");
                    scheduledFuture.cancel(true);
                }
            }
        });



        return root;
    }

    private void log(Object message) {
        System.out.printf("LowerFragment: %s\n", message);
    }
}
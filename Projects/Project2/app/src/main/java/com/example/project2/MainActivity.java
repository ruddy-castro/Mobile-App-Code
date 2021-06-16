/*
    Project 2
    Written by:
        Ly Do 018504783
        Ruddy Castro 026392117
        Ivy Nguyen 016618483
 */
package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private SharedData sharedData;

    private static final int animals[] = {R.drawable.animal13, R.drawable.animal14, R.drawable.animal15,
            R.drawable.animal16, R.drawable.animal17, R.drawable.animal18};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init shared data variable
        sharedData = new ViewModelProvider(this).get(SharedData.class);

        // Observe the selected index
        sharedData.getSelectedItemIndex().observe(this, index -> {
            // set selected image based on the selected index
            log("New selected index: " + index);
            sharedData.setSelectedItem(animals[index]);
        });

        // Initialize the selected item index to be 0 (first image)
        sharedData.setSelectedItemIndex(0);

        // Create fragment transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Create LowerFragment
        transaction.add(R.id.fragmentContainerView1, UpperFragment.class, null);

        // Create LowerFragment
        Bundle args2 = new Bundle();
        args2.putInt("numberOfImages", animals.length);
        transaction.add(R.id.fragmentContainerView2, LowerFragment.class, args2);

        // Commit transaction
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void log(Object message) {
        System.out.printf("MainActivity: %s\n", message);
    }
}
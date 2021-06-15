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

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private UpperFragment mUpperFrag;
    private LowerFragment mLowerFrag;

    int animals[] = {R.drawable.animal13, R.drawable.animal14, R.drawable.animal15,
            R.drawable.animal16, R.drawable.animal17, R.drawable.animal18};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Just change up the index to display any animal
        mUpperFrag = UpperFragment.newInstance("Image", animals[5]);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView1, mUpperFrag);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
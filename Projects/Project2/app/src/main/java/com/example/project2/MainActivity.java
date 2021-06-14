/*
    Project 2
    Written by:
        Ly Do 018504783
        Ruddy Castro 026392117
        Ivy Nguyen 016618483
 */
package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    GridView grdView;

    int animals[] = {R.drawable.animal13, R.drawable.animal14, R.drawable.animal15,
            R.drawable.animal16, R.drawable.animal17, R.drawable.animal18};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
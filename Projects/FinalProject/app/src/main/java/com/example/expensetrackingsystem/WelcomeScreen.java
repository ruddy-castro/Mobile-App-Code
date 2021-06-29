package com.example.expensetrackingsystem;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetrackingsystem.databinding.ActivityWelcomeScreenBinding;

public class WelcomeScreen extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityWelcomeScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWelcomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarWelcomeScreen.toolbar);
        binding.appBarWelcomeScreen.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_about)
                .setDrawerLayout(drawer)
                .build();

        // Controller that controls the navigation between frags
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_welcome_screen);

        // required to integrate navigation view with the action bar
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        // Setting up both the view and controller together
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    /**
     * Function that inflates the menu options when clicking the ellipse on the action bar.
     * @param menu the menu object with the choices for the ellipse
     * @return boolean value (true)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome_screen, menu);
        return true;
    }

    /**
     * Function that returns the user to the top level destination via the back button.
     * @return boolean value on if the user chooses to navigate up
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_welcome_screen);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;


import com.example.instagram.databinding.ActivityMainBinding;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarItemView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_logo_whiteout);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);



        final FragmentManager fragmentManager = getSupportFragmentManager();

        // define your fragments here
        final Fragment homeFragment = new FeedFragment();
        final Fragment composeFragment = new ComposeFragment();
        final Fragment profileFragment = new ProfileFragment();


        BottomNavigationView bottomNavigationView = binding.bottomNavigation;

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        item.setIcon(R.drawable.ic_baseline_home_24);
                        bottomNavigationView.getMenu().findItem(R.id.action_add).setIcon(R.drawable.ic_outline_add_box_24);
                        bottomNavigationView.getMenu().findItem(R.id.action_profile).setIcon(R.drawable.ic_baseline_person_outline_24);
                        fragment = homeFragment;
                        break;
                    case R.id.action_add:
                        bottomNavigationView.getMenu().findItem(R.id.action_home).setIcon(R.drawable.ic_outline_home_24);
                        item.setIcon(R.drawable.ic_baseline_add_box_24);
                        bottomNavigationView.getMenu().findItem(R.id.action_profile).setIcon(R.drawable.ic_baseline_person_outline_24);
                        fragment = composeFragment;
                        break;
                    case R.id.action_profile:
                        bottomNavigationView.getMenu().findItem(R.id.action_home).setIcon(R.drawable.ic_outline_home_24);
                        bottomNavigationView.getMenu().findItem(R.id.action_add).setIcon(R.drawable.ic_outline_add_box_24);
                        item.setIcon(R.drawable.ic_baseline_person_24);
                        fragment = profileFragment;
                        break;
                    default:
                        bottomNavigationView.getMenu().findItem(R.id.action_home).setIcon(R.drawable.ic_outline_home_24);
                        bottomNavigationView.getMenu().findItem(R.id.action_add).setIcon(R.drawable.ic_outline_add_box_24);
                        item.setIcon(R.drawable.ic_baseline_person_24);
                        fragment = profileFragment;
                        break;


                }

                fragmentManager.beginTransaction().replace(R.id.rlContainer, fragment).commit();
                return true;
            }
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.log_out){
            ParseUser.logOut();
            ParseUser.getCurrentUser();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
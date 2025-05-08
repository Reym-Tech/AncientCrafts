package com.example.ancientcrafts;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ancientcrafts.fragments.HomePage;
import com.example.ancientcrafts.fragments.NotificationPage;
import com.example.ancientcrafts.fragments.ProfilePage;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make activity full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home);

        bottomNav = findViewById(R.id.bottom_navigation);

        // Set home as default selected item
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Load home fragment by default if no saved instance state
        if (savedInstanceState == null) {
            loadFragment(new HomePage());
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    selectedFragment = new HomePage();
                } else if (itemId == R.id.nav_notif) {
                    selectedFragment = new NotificationPage();
                } else if (itemId == R.id.nav_profile) {
                    selectedFragment = new ProfilePage();
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                }
                return true;
            };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null); // Optional: Add to back stack
        transaction.commit();
    }
}
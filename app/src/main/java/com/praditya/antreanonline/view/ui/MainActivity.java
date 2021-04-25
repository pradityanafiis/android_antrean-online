package com.praditya.antreanonline.view.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.praditya.antreanonline.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private long backPressedTime;
    @BindView(R.id.bottom_navigation_menu)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        if (getIntent().getExtras() != null) {
            switch (getIntent().getStringExtra("active")) {
                case "home":
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    bottomNavigationView.setSelectedItemId(R.id.nav_home);
                    break;
                case "queue":
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QueueFragment()).commit();
                    bottomNavigationView.setSelectedItemId(R.id.nav_queue);
                    break;
                case "profile":
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                    bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                    break;
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.nav_queue:
                    fragment = new QueueFragment();
                    break;
                case R.id.nav_profile:
                    fragment = new ProfileFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            showMessage("Tekan tombol kembali lagi untuk keluar.");
        }
        backPressedTime = System.currentTimeMillis();
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }
}
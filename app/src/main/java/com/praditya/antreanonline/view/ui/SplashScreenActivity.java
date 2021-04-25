package com.praditya.antreanonline.view.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.praditya.antreanonline.R;
import com.praditya.antreanonline.storage.SharedPreferencesManager;
import com.praditya.antreanonline.view.ui.MainActivity;
import com.praditya.antreanonline.view.ui.auth.AuthActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPreferencesManager.getSharedPreferencesManager(SplashScreenActivity.this).isLogin()) {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, IntroActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }
}
package com.praditya.antreanonline.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroFragment;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.view.ui.auth.AuthActivity;

import org.jetbrains.annotations.Nullable;

public class IntroActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(AppIntroFragment.newInstance(
                "HEMAT WAKTUMU TANPA MENGANTRE",
                "Dengan Antrean Online, kamu gak perlu lagi menghabiskan waktumu dengan sia-sia.",
                R.drawable.intro_5,
                Color.parseColor("#FFFFFF"),
                Color.parseColor("#47B2E5"),
                Color.parseColor("#000000")
        ));
        addSlide(AppIntroFragment.newInstance(
                "AMBIL DAN TUNGGU ANTREANMU DIMANA SAJA",
                "Antrean Online akan memberikan estimasi waktu untukmu, agar kamu dapat datang saat giliranmu mulai!",
                R.drawable.intro_6,
                Color.parseColor("#47B2E5"),
                Color.parseColor("#FFFFFF"),
                Color.parseColor("#FFFFFF")
        ));
        setColorTransitionsEnabled(true);
        setImmersiveMode();
    }

    @Override
    protected void onSkipPressed(@Nullable Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(IntroActivity.this, AuthActivity.class));
        finish();
    }

    @Override
    protected void onDonePressed(@Nullable Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(IntroActivity.this, AuthActivity.class));
        finish();
    }
}
package com.praditya.antreanonline.view.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.praditya.antreanonline.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
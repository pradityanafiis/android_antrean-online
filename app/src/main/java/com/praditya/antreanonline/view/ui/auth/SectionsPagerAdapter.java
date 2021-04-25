package com.praditya.antreanonline.view.ui.auth;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.praditya.antreanonline.view.ui.auth.LoginFragment;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.view.ui.auth.RegisterFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context context;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new LoginFragment();
                break;
            case 1:
                fragment = new RegisterFragment();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence charSequence = "";
        switch (position) {
            case 0:
                charSequence = "Masuk";
                break;
            case 1:
                charSequence = "Daftar";
                break;
        }
        return charSequence;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
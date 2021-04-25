package com.praditya.antreanonline.view.ui.queue;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.praditya.antreanonline.view.ui.queue.ActiveQueueFragment;
import com.praditya.antreanonline.view.ui.queue.HistoryQueueFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public SectionsPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ActiveQueueFragment();
                break;
            case 1:
                fragment = new HistoryQueueFragment();
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
                charSequence = "Aktif";
                break;
            case 1:
                charSequence = "Riwayat";
                break;
        }
        return charSequence;
    }

    @Override
    public int getCount() {
        return 2;
    }
}

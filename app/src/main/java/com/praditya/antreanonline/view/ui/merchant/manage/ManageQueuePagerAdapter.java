package com.praditya.antreanonline.view.ui.merchant.manage;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ManageQueuePagerAdapter extends FragmentPagerAdapter {

    private final Context context;

    public ManageQueuePagerAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ManageWaitingQueueFragment();
                break;
            case 1:
                fragment = new ManageHistoryQueueFragment();
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
                charSequence = "Menunggu";
                break;
            case 1:
                charSequence = "Riwayat";
        }
        return charSequence;
    }

    @Override
    public int getCount() {
        return 2;
    }
}

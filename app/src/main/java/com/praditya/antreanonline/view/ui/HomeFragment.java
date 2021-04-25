package com.praditya.antreanonline.view.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.Category;
import com.praditya.antreanonline.storage.SharedPreferencesManager;
import com.praditya.antreanonline.view.ui.auth.AuthActivity;
import com.praditya.antreanonline.view.ui.merchant.MerchantByCategoryActivity;
import com.praditya.antreanonline.view.ui.merchant.SearchMerchantActivity;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    @OnClick(R.id.cv_search)
    void onSearchClick() {
        startActivity(new Intent(this.getContext(), SearchMerchantActivity.class));
    }
    @OnClick({R.id.menu_bengkel, R.id.menu_cuci_kendaraan, R.id.menu_kecantikan, R.id.menu_kesehatan, R.id.menu_makanan, R.id.menu_pelayanan_publik, R.id.menu_pendidikan, R.id.menu_perbankan, R.id.menu_umum})
    void menuOnClick(View view) {
        Intent intent = new Intent(this.getActivity(), MerchantByCategoryActivity.class);
        switch (view.getId()) {
            case R.id.menu_bengkel:
                intent.putExtra("category", Category.BENGKEL);
                break;
            case R.id.menu_cuci_kendaraan:
                intent.putExtra("category", Category.CUCI_KENDARAAN);
                break;
            case R.id.menu_kecantikan:
                intent.putExtra("category", Category.KECANTIKAN);
                break;
            case R.id.menu_kesehatan:
                intent.putExtra("category", Category.KESEHATAN);
                break;
            case R.id.menu_makanan:
                intent.putExtra("category", Category.MAKANAN);
                break;
            case R.id.menu_pelayanan_publik:
                intent.putExtra("category", Category.PELAYANAN_PUBLIK);
                break;
            case R.id.menu_pendidikan:
                intent.putExtra("category", Category.PENDIDIKAN);
                break;
            case R.id.menu_perbankan:
                intent.putExtra("category", Category.PERBANKAN);
                break;
            case R.id.menu_umum:
                intent.putExtra("category", Category.UMUM);
                break;
        }
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
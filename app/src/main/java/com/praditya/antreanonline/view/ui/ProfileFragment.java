package com.praditya.antreanonline.view.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.api.ApiClient;
import com.praditya.antreanonline.api.Services;
import com.praditya.antreanonline.api.response.SingleResponse;
import com.praditya.antreanonline.model.Merchant;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.model.User;
import com.praditya.antreanonline.storage.SharedPreferencesManager;
import com.praditya.antreanonline.view.ui.auth.AuthActivity;
import com.praditya.antreanonline.view.ui.merchant.create.CreateMerchantActivity;
import com.praditya.antreanonline.view.ui.merchant.manage.ManageBusinessMenuActivity;
import com.praditya.antreanonline.viewmodel.MerchantViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private final String TOKEN;
    private final Services services;
    private User user;
    @BindView(R.id.iv_profile_photo)
    CircleImageView imageView;
    @BindView(R.id.tv_name)
    MaterialTextView tvName;
    @BindView(R.id.tv_title_queue_menu)
    MaterialTextView tvTitleQueueMenu;
    @BindView(R.id.cv_queue_menu)
    MaterialCardView cvQueueMenu;
    @OnClick(R.id.cv_edit_profile)
    void editProfile() {
        startActivity(new Intent(this.getActivity(), EditAccountActivity.class));
    }
    @OnClick(R.id.cv_change_password)
    void changePassword() {
        startActivity(new Intent(this.getActivity(), ChangePasswordActivity.class));
    }
    @OnClick(R.id.cv_queue_menu)
    void openMenu() {
        if (user.isHasMerchant()) {
            startActivity(new Intent(getActivity(), ManageBusinessMenuActivity.class));
        } else {
            Intent intent = new Intent(this.getActivity(), CreateMerchantActivity.class);
            startActivity(intent);
        }
    }
    @OnClick(R.id.cv_logout)
    void onClickLogout() {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this.getContext());
        materialAlertDialogBuilder
                .setTitle("Keluar")
                .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
                .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logout();
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }
    @BindView(R.id.loading)
    ProgressBar loading;

    public ProfileFragment() {
        TOKEN = SharedPreferencesManager.getSharedPreferencesManager(getContext()).getToken();
        services = ApiClient.getServices();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        user = SharedPreferencesManager.getSharedPreferencesManager(getContext()).getUser();
        tvName.setText(user.getName());

        if (user.isHasMerchant()) {
            tvTitleQueueMenu.setText("Kelola Bisnis Anda");
        } else {
            tvTitleQueueMenu.setText("Daftarkan Bisnis Anda");
        }

        if (user.getPhoto() != null) {
            Glide.with(this).load(decodeImage(user.getPhoto())).centerCrop().into(imageView);
        }
    }

    private void logout() {
        showLoading(true);
        services.logout(TOKEN).enqueue(new Callback<SingleResponse<User>>() {
            @Override
            public void onResponse(Call<SingleResponse<User>> call, Response<SingleResponse<User>> response) {
                boolean error = response.body().isError();
                if (!error) {
                    SharedPreferencesManager.getSharedPreferencesManager(getContext()).deleteUser();
                    Intent intent = new Intent(getContext(), AuthActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    showMessage(response.body().getMessage());
                }
                showLoading(false);
            }

            @Override
            public void onFailure(Call<SingleResponse<User>> call, Throwable t) {
                t.printStackTrace();
                showMessage(t.getMessage());
                showLoading(false);
            }
        });
    }

    private Bitmap decodeImage(String image) {
        byte[] imageBytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    private void showLoading(boolean state) {
        if (state)
            loading.setVisibility(View.VISIBLE);
        else
            loading.setVisibility(View.GONE);
    }

    private void showMessage(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
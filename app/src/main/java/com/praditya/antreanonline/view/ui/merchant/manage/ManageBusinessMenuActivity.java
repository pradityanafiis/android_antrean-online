package com.praditya.antreanonline.view.ui.merchant.manage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.Merchant;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.view.ui.merchant.manage.service.ManageServiceActivity;
import com.praditya.antreanonline.viewmodel.MerchantViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ManageBusinessMenuActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private Merchant merchant;
    private MerchantViewModel viewModel;
    @BindView(R.id.layout_main)
    LinearLayout layoutMain;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.app_bar)
    Toolbar toolbar;
    @BindView(R.id.iv_merchant_photo)
    CircleImageView imageView;
    @BindView(R.id.tv_merchant_name)
    MaterialTextView tvMerchantName;
    @OnClick(R.id.cv_edit_merchant)
    void openEditMerchant() {
        startActivity(new Intent(ManageBusinessMenuActivity.this, EditMerchantInfoActivity.class));
    }
    @OnClick(R.id.cv_set_businesshours)
    void openSetBusinessHours() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("business_hours", merchant.getBusinessHours());
        Intent intent = new Intent(ManageBusinessMenuActivity.this, SetBusinessHoursActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @OnClick(R.id.cv_manage_service)
    void openManageService() {
        startActivity(new Intent(ManageBusinessMenuActivity.this, ManageServiceActivity.class));
    }
    @OnClick(R.id.cv_manage_queue)
    void openManageQueue() {
        startActivity(new Intent(ManageBusinessMenuActivity.this, ManageQueueActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_business_menu);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        viewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(MerchantViewModel.class);
        getMerchant();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMerchant();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getMerchant() {
        showLoading(true);
        viewModel.findByUser().observe(this, new Observer<Resource<Merchant>>() {
            @Override
            public void onChanged(Resource<Merchant> merchantResource) {
                switch (merchantResource.getStatus()) {
                    case SUCCESS:
                        merchant = merchantResource.getData();
                        initView(merchant);
                        showLoading(false);
                        break;
                    case EMPTY:
                    case ERROR:
                        showLoading(false);
                        break;
                }
            }
        });
    }

    private void initView(Merchant merchant) {
        tvMerchantName.setText(merchant.getName());
        Glide.with(this).load(decodeImage(merchant.getPhoto())).centerCrop().into(imageView);
    }

    private Bitmap decodeImage(String image) {
        byte[] imageBytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    private void showLoading(boolean state) {
        if (state) {
            layoutMain.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
        } else {
            layoutMain.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
        }
    }
}
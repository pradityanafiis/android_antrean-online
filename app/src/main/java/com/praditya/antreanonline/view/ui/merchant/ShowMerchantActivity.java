package com.praditya.antreanonline.view.ui.merchant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.Merchant;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.model.Service;
import com.praditya.antreanonline.view.adapter.MerchantAdapter;
import com.praditya.antreanonline.view.adapter.ServiceAdapter;
import com.praditya.antreanonline.view.dialog.BusinessHoursBottomSheet;
import com.praditya.antreanonline.view.ui.DividerItemDecoration;
import com.praditya.antreanonline.viewmodel.MerchantViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowMerchantActivity extends AppCompatActivity implements ServiceAdapter.ServiceOnClickCallback {

    private final String TAG = getClass().getSimpleName();
    private Merchant merchant;
    private ServiceAdapter serviceAdapter;
    private MerchantViewModel viewModel;
    @BindView(R.id.layout_main)
    LinearLayout layoutMain;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.app_bar)
    Toolbar toolbar;
    @BindView(R.id.iv_merchant_photo)
    ImageView imageView;
    @BindView(R.id.tv_merchant_name)
    TextView tvMerchantName;
    @BindView(R.id.tv_merchant_address)
    TextView tvMerchantAddress;
    @BindView(R.id.rv_service)
    RecyclerView recyclerView;
    @OnClick(R.id.btn_businesshours)
    void openBusinessHours() {
        BusinessHoursBottomSheet bottomSheet = new BusinessHoursBottomSheet(merchant.getBusinessHours());
        bottomSheet.show(getSupportFragmentManager(), "Business Hours Bottom Sheet");
    }
    @OnClick(R.id.btn_phone)
    void openPhone() {
        Uri uri = Uri.parse(merchant.getPhoneUri());
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(intent);
    }
    @OnClick(R.id.btn_location)
    void openLocation() {
        Uri uri = Uri.parse(merchant.getLocationUri());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_merchant);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        viewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(MerchantViewModel.class);
        getMerchant(getIntent().getIntExtra("merchant_id", 0));
    }

    private void getMerchant(int merchantId) {
        showLoading(true);
        viewModel.findById(merchantId).observe(ShowMerchantActivity.this, new Observer<Resource<Merchant>>() {
            @Override
            public void onChanged(Resource<Merchant> merchantResource) {
                switch (merchantResource.getStatus()) {
                    case SUCCESS:
                        merchant = merchantResource.getData();
                        initView(merchant);
                        showLoading(false);
                        break;
                    case ERROR:
                    case EMPTY:
                        showMessage(merchantResource.getMessage());
                        break;
                }
            }
        });
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

    @Override
    public void chooseService(Service service) {
        Intent intent = new Intent(ShowMerchantActivity.this, ChooseDateActivity.class);
        intent.putExtra("service", service);
        merchant.setPhoto(null);
        intent.putExtra("merchant", merchant);
        startActivity(intent);
    }

    private void initView(Merchant merchant) {
        Glide.with(this).load(decodeImage(merchant.getPhoto())).centerCrop().into(imageView);
        tvMerchantName.setText(merchant.getName());
        tvMerchantAddress.setText(merchant.getAddress());

        serviceAdapter = new ServiceAdapter(ShowMerchantActivity.this);
        serviceAdapter.setServiceOnClickCallback(this);
        serviceAdapter.setServices(merchant.getServices());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ShowMerchantActivity.this));
        recyclerView.setAdapter(serviceAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(ShowMerchantActivity.this, R.drawable.divider)));
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
    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
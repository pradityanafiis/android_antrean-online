package com.praditya.antreanonline.view.ui.merchant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.Category;
import com.praditya.antreanonline.model.Merchant;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.repository.MerchantRepository;
import com.praditya.antreanonline.view.adapter.MerchantAdapter;
import com.praditya.antreanonline.viewmodel.MerchantViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MerchantByCategoryActivity extends AppCompatActivity implements MerchantAdapter.OnClickCallback {

    private final String TAG = getClass().getSimpleName();
    private MerchantViewModel merchantViewModel;
    private MerchantAdapter merchantAdapter;
    @BindView(R.id.app_bar)
    Toolbar toolbar;
    @BindView(R.id.rv_merchant)
    RecyclerView recyclerView;
    @BindView(R.id.cv_empty_message)
    MaterialCardView cvEmptyMessage;
    @BindView(R.id.loading)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_by_category);
        ButterKnife.bind(this);
        initView();
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search_menu, menu);
//        return true;
//    }

    private void initView() {
        Category category = (Category) getIntent().getSerializableExtra("category");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(category.getName());

        merchantAdapter = new MerchantAdapter(MerchantByCategoryActivity.this);
        merchantAdapter.setOnClickCallback(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MerchantByCategoryActivity.this));
        recyclerView.setAdapter(merchantAdapter);
        merchantViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(MerchantViewModel.class);
        merchantViewModel.setMerchantCategory(category.getName());
    }

    private void getMerchant() {
        showRecyclerView(false);
        showEmptyMessage(false);
        showLoading(true);
        merchantViewModel.findMerchantByCategory().observe(MerchantByCategoryActivity.this, new Observer<Resource<ArrayList<Merchant>>>() {
            @Override
            public void onChanged(Resource<ArrayList<Merchant>> arrayListResource) {
                switch (arrayListResource.getStatus()) {
                    case SUCCESS:
                        merchantAdapter.setMerchants(arrayListResource.getData());
                        showRecyclerView(true);
                        showEmptyMessage(false);
                        break;
                    case EMPTY:
                        showRecyclerView(false);
                        showEmptyMessage(true);
                        break;
                    case ERROR:
                        showRecyclerView(false);
                        showEmptyMessage(false);
                        showMessage(arrayListResource.getMessage());
                        break;
                }
                showLoading(false);
            }
        });
    }

    @Override
    public void showMerchant(Merchant merchant) {
        Intent intent = new Intent(MerchantByCategoryActivity.this, ShowMerchantActivity.class);
        intent.putExtra("merchant_id", merchant.getId());
        startActivity(intent);
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private void showLoading(boolean state) {
        if (state)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }

    private void showRecyclerView(boolean state) {
        if (state)
            recyclerView.setVisibility(View.VISIBLE);
        else
            recyclerView.setVisibility(View.INVISIBLE);
    }

    private void showEmptyMessage(boolean state) {
        if (state)
            cvEmptyMessage.setVisibility(View.VISIBLE);
        else
            cvEmptyMessage.setVisibility(View.GONE);
    }
}
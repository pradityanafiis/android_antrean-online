package com.praditya.antreanonline.view.ui.merchant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.Merchant;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.view.adapter.MerchantAdapter;
import com.praditya.antreanonline.viewmodel.MerchantViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class SearchMerchantActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private MerchantViewModel merchantViewModel;
    private MerchantAdapter merchantAdapter;
    @BindView(R.id.app_bar)
    Toolbar toolbar;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.rv_merchant)
    RecyclerView recyclerView;
    @BindView(R.id.cv_empty_message)
    MaterialCardView cvEmptyMessage;
    @BindView(R.id.loading)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_merchant);
        ButterKnife.bind(this);
        initView();
        searchView.setOnQueryTextListener(onQueryTextListener);
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

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        merchantAdapter = new MerchantAdapter(SearchMerchantActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchMerchantActivity.this));
        recyclerView.setAdapter(merchantAdapter);
        merchantViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(MerchantViewModel.class);
        merchantViewModel.findMerchantByName().observe(SearchMerchantActivity.this, new Observer<Resource<ArrayList<Merchant>>>() {
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

        showRecyclerView(false);
        showEmptyMessage(false);
        showLoading(false);
    }

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            showRecyclerView(false);
            showEmptyMessage(false);
            showLoading(true);
            merchantViewModel.setMerchantName(query);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            //showLoading(true);
            //merchantViewModel.setMerchantName(newText);
            return true;
        }
    };

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
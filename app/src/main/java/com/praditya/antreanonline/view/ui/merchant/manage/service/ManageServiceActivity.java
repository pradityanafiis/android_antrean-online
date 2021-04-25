package com.praditya.antreanonline.view.ui.merchant.manage.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.model.Service;
import com.praditya.antreanonline.view.adapter.ManageServiceAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageServiceActivity extends AppCompatActivity implements ManageServiceAdapter.OnClickCallback, ManageServiceDialog.ServiceDialogListener {

    private final String TAG = getClass().getSimpleName();
    private ManageServiceViewModel viewModel;
    private ManageServiceAdapter adapter;
    private ManageServiceDialog dialog;
    @BindView(R.id.app_bar)
    Toolbar toolbar;
    @BindView(R.id.rv_manage_service)
    RecyclerView recyclerView;
    @OnClick(R.id.fab_create_service)
    void createService() {
        dialog = new ManageServiceDialog(ManageServiceActivity.this,"Tambah Layanan", false);
        dialog.setListener(this);
        dialog.show(getSupportFragmentManager(), "Create Service");
    }
    @BindView(R.id.cv_empty_message)
    MaterialCardView cvEmptyMessage;
    @BindView(R.id.loading)
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_service);
        ButterKnife.bind(this);
        viewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(ManageServiceViewModel.class);
        adapter = new ManageServiceAdapter(ManageServiceActivity.this);
        adapter.setOnClickCallback(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        showLoading(false);
        showRecyclerView(false);
        showEmptyMessage(false);
        refresh();
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

    private void refresh() {
        showLoading(true);
        viewModel.findServiceByMerchant().observe(ManageServiceActivity.this, new Observer<Resource<ArrayList<Service>>>() {
            @Override
            public void onChanged(Resource<ArrayList<Service>> arrayListResource) {
                switch (arrayListResource.getStatus()) {
                    case SUCCESS:
                        adapter.setServices(arrayListResource.getData());
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
    public void createService(Service service) {
        viewModel.create(service).observe(ManageServiceActivity.this, new Observer<Resource<Service>>() {
            @Override
            public void onChanged(Resource<Service> serviceResource) {
                switch (serviceResource.getStatus()) {
                    case SUCCESS:
                        refresh();
                        break;
                    case EMPTY:
                    case ERROR:
                        showMessage(serviceResource.getMessage());
                        break;
                }
            }
        });
    }

    @Override
    public void showUpdateServiceDialog(Service service) {
        dialog = new ManageServiceDialog(ManageServiceActivity.this, "Sunting Layanan", true);
        dialog.setListener(this);
        dialog.setService(service);
        dialog.show(getSupportFragmentManager(), "Update Service");
    }

    @Override
    public void updateService(Service service) {
        viewModel.update(service).observe(ManageServiceActivity.this, new Observer<Resource<Service>>() {
            @Override
            public void onChanged(Resource<Service> serviceResource) {
                switch (serviceResource.getStatus()) {
                    case SUCCESS:
                        refresh();
                        break;
                    case EMPTY:
                    case ERROR:
                        showMessage(serviceResource.getMessage());
                        break;
                }
            }
        });
    }

    private void showLoading(boolean state) {
        if (state)
            loading.setVisibility(View.VISIBLE);
        else
            loading.setVisibility(View.GONE);
    }

    private void showRecyclerView(boolean state) {
        if (state)
            recyclerView.setVisibility(View.VISIBLE);
        else
            recyclerView.setVisibility(View.GONE);
    }

    private void showEmptyMessage(boolean state) {
        if (state)
            cvEmptyMessage.setVisibility(View.VISIBLE);
        else
            cvEmptyMessage.setVisibility(View.GONE);
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
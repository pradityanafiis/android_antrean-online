package com.praditya.antreanonline.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.praditya.antreanonline.api.ApiClient;
import com.praditya.antreanonline.api.Services;
import com.praditya.antreanonline.api.response.MultipleResponses;
import com.praditya.antreanonline.api.response.SingleResponse;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.model.Service;
import com.praditya.antreanonline.model.Status;
import com.praditya.antreanonline.storage.SharedPreferencesManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceRepository {

    private final String TAG = getClass().getSimpleName();
    private final String TOKEN;
    private final Services services;

    public ServiceRepository(Application application) {
        TOKEN = SharedPreferencesManager.getSharedPreferencesManager(application).getToken();
        services = ApiClient.getServices();
    }

    public LiveData<Resource<ArrayList<Service>>> findServiceByMerchant() {
        final MutableLiveData<Resource<ArrayList<Service>>> data = new MutableLiveData<>();
        services.findServiceByMerchant(TOKEN).enqueue(new Callback<MultipleResponses<Service>>() {
            @Override
            public void onResponse(Call<MultipleResponses<Service>> call, Response<MultipleResponses<Service>> response) {
                MultipleResponses<Service> multipleResponses = response.body();
                if (!multipleResponses.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, multipleResponses.getMessage(), multipleResponses.getData()));
                } else {
                    data.setValue(new Resource<>(Status.EMPTY, multipleResponses.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<MultipleResponses<Service>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }

    public LiveData<Resource<Service>> create(Service service) {
        final MutableLiveData<Resource<Service>> data = new MutableLiveData<>();
        services.createService(TOKEN, service).enqueue(new Callback<SingleResponse<Service>>() {
            @Override
            public void onResponse(Call<SingleResponse<Service>> call, Response<SingleResponse<Service>> response) {
                SingleResponse<Service> singleResponse = response.body();
                if (!singleResponse.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, singleResponse.getMessage(), singleResponse.getData()));
                } else {
                    data.setValue(new Resource<>(Status.EMPTY, singleResponse.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<Service>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }

    public LiveData<Resource<Service>> update(Service service) {
        final MutableLiveData<Resource<Service>> data = new MutableLiveData<>();
        services.updateService(TOKEN, service).enqueue(new Callback<SingleResponse<Service>>() {
            @Override
            public void onResponse(Call<SingleResponse<Service>> call, Response<SingleResponse<Service>> response) {
                SingleResponse<Service> singleResponse = response.body();
                if (!singleResponse.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, singleResponse.getMessage(), singleResponse.getData()));
                } else {
                    data.setValue(new Resource<>(Status.EMPTY, singleResponse.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<Service>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }
}
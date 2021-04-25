package com.praditya.antreanonline.repository;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.praditya.antreanonline.api.ApiClient;
import com.praditya.antreanonline.api.Services;
import com.praditya.antreanonline.api.response.MultipleResponses;
import com.praditya.antreanonline.api.response.SingleResponse;
import com.praditya.antreanonline.model.BusinessHour;
import com.praditya.antreanonline.model.Category;
import com.praditya.antreanonline.model.Merchant;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.model.Status;
import com.praditya.antreanonline.storage.SharedPreferencesManager;
import com.praditya.antreanonline.view.ui.merchant.SearchMerchantActivity;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerchantRepository {

    private final String TOKEN;
    private Services services;
    private boolean error = false;

    public MerchantRepository(Application application) {
        this.services = ApiClient.getServices();
        this.TOKEN = SharedPreferencesManager.getSharedPreferencesManager(application).getToken();
    }

    public LiveData<Resource<ArrayList<Merchant>>> findByName(String name) {
        final MutableLiveData<Resource<ArrayList<Merchant>>> data = new MutableLiveData<>();
        services.findMerchantByName(TOKEN, name).enqueue(new Callback<MultipleResponses<Merchant>>() {
            @Override
            public void onResponse(Call<MultipleResponses<Merchant>> call, Response<MultipleResponses<Merchant>> response) {
                MultipleResponses<Merchant> responses = response.body();
                if (!responses.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, responses.getMessage(), responses.getData()));
                } else {
                    data.setValue(new Resource<>(Status.EMPTY, responses.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<MultipleResponses<Merchant>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }

    public LiveData<Resource<ArrayList<Merchant>>> findByCategory(String category) {
        final MutableLiveData<Resource<ArrayList<Merchant>>> data = new MutableLiveData<>();
        services.findMerchantByCategory(TOKEN, category).enqueue(new Callback<MultipleResponses<Merchant>>() {
            @Override
            public void onResponse(Call<MultipleResponses<Merchant>> call, Response<MultipleResponses<Merchant>> response) {
                MultipleResponses<Merchant> multipleResponses = response.body();
                if (!multipleResponses.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, multipleResponses.getMessage(), multipleResponses.getData()));
                } else {
                    data.setValue(new Resource<>(Status.EMPTY, multipleResponses.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<MultipleResponses<Merchant>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }

    public LiveData<Resource<Merchant>> findByUser() {
        final MutableLiveData<Resource<Merchant>> data = new MutableLiveData<>();
        services.findMerchantByUser(TOKEN).enqueue(new Callback<SingleResponse<Merchant>>() {
            @Override
            public void onResponse(Call<SingleResponse<Merchant>> call, Response<SingleResponse<Merchant>> response) {
                SingleResponse<Merchant> singleResponse = response.body();
                if (!singleResponse.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, singleResponse.getMessage(), singleResponse.getData()));
                } else {
                    data.setValue(new Resource<>(Status.EMPTY, singleResponse.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<Merchant>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }

    public LiveData<Resource<Merchant>> findById(int merchantId) {
        final MutableLiveData<Resource<Merchant>> data = new MutableLiveData<>();
        services.findMerchantById(TOKEN, merchantId).enqueue(new Callback<SingleResponse<Merchant>>() {
            @Override
            public void onResponse(Call<SingleResponse<Merchant>> call, Response<SingleResponse<Merchant>> response) {
                SingleResponse<Merchant> singleResponse = response.body();
                if (!singleResponse.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, singleResponse.getMessage(), singleResponse.getData()));
                } else {
                    data.setValue(new Resource<>(Status.ERROR, singleResponse.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<Merchant>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }

    public LiveData<Resource<Merchant>> create(Merchant merchant) {
        final MutableLiveData<Resource<Merchant>> data = new MutableLiveData<>();
        services.createMerchant(TOKEN, merchant).enqueue(new Callback<SingleResponse<Merchant>>() {
            @Override
            public void onResponse(Call<SingleResponse<Merchant>> call, Response<SingleResponse<Merchant>> response) {
                SingleResponse<Merchant> responses = response.body();
                if (!responses.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, responses.getMessage(), responses.getData()));
                } else {
                    data.setValue(new Resource<>(Status.ERROR, responses.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<Merchant>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }

    public LiveData<Resource<Merchant>> update(Merchant merchant) {
        final MutableLiveData<Resource<Merchant>> data = new MutableLiveData<>();
        services.updateMerchant(TOKEN, merchant).enqueue(new Callback<SingleResponse<Merchant>>() {
            @Override
            public void onResponse(Call<SingleResponse<Merchant>> call, Response<SingleResponse<Merchant>> response) {
                SingleResponse<Merchant> singleResponse = response.body();
                if (!singleResponse.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, singleResponse.getMessage(), singleResponse.getData()));
                } else {
                    data.setValue(new Resource<>(Status.ERROR, singleResponse.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<Merchant>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }

    public LiveData<Resource<Merchant>> setBusinessHours(Merchant merchant) {
        final MutableLiveData<Resource<Merchant>> data = new MutableLiveData<>();
        services.setBusinessHour(TOKEN, merchant).enqueue(new Callback<SingleResponse<Merchant>>() {
            @Override
            public void onResponse(Call<SingleResponse<Merchant>> call, Response<SingleResponse<Merchant>> response) {
                SingleResponse<Merchant> singleResponse = response.body();
                if (!singleResponse.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, singleResponse.getMessage(), singleResponse.getData()));
                } else {
                    data.setValue(new Resource<>(Status.ERROR, singleResponse.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<Merchant>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }
}

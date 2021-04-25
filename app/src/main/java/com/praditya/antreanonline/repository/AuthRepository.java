package com.praditya.antreanonline.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.praditya.antreanonline.api.ApiClient;
import com.praditya.antreanonline.api.Services;
import com.praditya.antreanonline.api.response.SingleResponse;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.model.Status;
import com.praditya.antreanonline.model.User;
import com.praditya.antreanonline.storage.SharedPreferencesManager;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private final String TOKEN;
    private final Services services;

    public AuthRepository(Application application) {
        TOKEN = SharedPreferencesManager.getSharedPreferencesManager(application).getToken();
        services = ApiClient.getServices();
    }

    public LiveData<Resource<User>> update(User user) {
        final MutableLiveData<Resource<User>> data = new MutableLiveData<>();
        services.updateUser(TOKEN, user).enqueue(new Callback<SingleResponse<User>>() {
            @Override
            public void onResponse(Call<SingleResponse<User>> call, Response<SingleResponse<User>> response) {
                SingleResponse<User> singleResponse = response.body();
                if (!singleResponse.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, singleResponse.getMessage(), singleResponse.getData()));
                } else {
                    data.setValue(new Resource<>(Status.ERROR, singleResponse.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<User>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }

    public LiveData<Resource<User>> changePassword(String currentPassword, String newPassword) {
        final MutableLiveData<Resource<User>> data = new MutableLiveData<>();
        services.changePassword(TOKEN, currentPassword, newPassword).enqueue(new Callback<SingleResponse<User>>() {
            @Override
            public void onResponse(Call<SingleResponse<User>> call, Response<SingleResponse<User>> response) {
                SingleResponse<User> singleResponse = response.body();
                if (!singleResponse.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, singleResponse.getMessage(), singleResponse.getData()));
                } else {
                    data.setValue(new Resource<>(Status.ERROR, singleResponse.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<User>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }
}

package com.praditya.antreanonline.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.praditya.antreanonline.api.ApiClient;
import com.praditya.antreanonline.api.Services;
import com.praditya.antreanonline.api.response.MultipleResponses;
import com.praditya.antreanonline.api.response.SingleResponse;
import com.praditya.antreanonline.model.EstimatedQueue;
import com.praditya.antreanonline.model.Queue;
import com.praditya.antreanonline.model.QueueStatus;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.model.Service;
import com.praditya.antreanonline.model.Status;
import com.praditya.antreanonline.storage.SharedPreferencesManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class QueueRepository {

    private final String TAG = getClass().getSimpleName();
    private final String TOKEN;
    private Services services;

    public QueueRepository(Application application) {
        services = ApiClient.getServices();
        TOKEN = SharedPreferencesManager.getSharedPreferencesManager(application).getToken();
    }

    public LiveData<Resource<EstimatedQueue>> findByDate(int serviceId, String date) {
        final MutableLiveData<Resource<EstimatedQueue>> data = new MutableLiveData<>();
        services.findQueueByDate(TOKEN, serviceId, date).enqueue(new Callback<SingleResponse<EstimatedQueue>>() {
            @Override
            public void onResponse(Call<SingleResponse<EstimatedQueue>> call, Response<SingleResponse<EstimatedQueue>> response) {
                SingleResponse<EstimatedQueue> singleResponse = response.body();
                if (!singleResponse.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, singleResponse.getMessage(), singleResponse.getData()));
                } else {
                    data.setValue(new Resource<>(Status.EMPTY, singleResponse.getMessage(), singleResponse.getData()));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<EstimatedQueue>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }

    public MutableLiveData<Resource<Queue>> create(Queue queue) {
        final MutableLiveData<Resource<Queue>> data = new MutableLiveData<>();
        services.createQueue(TOKEN, queue).enqueue(new Callback<SingleResponse<Queue>>() {
            @Override
            public void onResponse(Call<SingleResponse<Queue>> call, Response<SingleResponse<Queue>> response) {
                SingleResponse<Queue> responses = response.body();
                if (!responses.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, responses.getMessage(), responses.getData()));
                } else {
                    data.setValue(new Resource<>(Status.ERROR, responses.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<Queue>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }

    public MutableLiveData<Resource<ArrayList<Queue>>> findActiveByUser() {
        final MutableLiveData<Resource<ArrayList<Queue>>> data = new MutableLiveData<>();
        services.findActiveQueueByUser(TOKEN).enqueue(new Callback<MultipleResponses<Queue>>() {
            @Override
            public void onResponse(Call<MultipleResponses<Queue>> call, Response<MultipleResponses<Queue>> response) {
                MultipleResponses<Queue> responses = response.body();
                if (!responses.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, responses.getMessage(), responses.getData()));
                } else {
                    data.setValue(new Resource<>(Status.EMPTY, responses.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<MultipleResponses<Queue>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }

    public MutableLiveData<Resource<ArrayList<Queue>>> findHistoryByUser() {
        final MutableLiveData<Resource<ArrayList<Queue>>> data = new MutableLiveData<>();
        services.findHistoryQueueByUser(TOKEN).enqueue(new Callback<MultipleResponses<Queue>>() {
            @Override
            public void onResponse(Call<MultipleResponses<Queue>> call, Response<MultipleResponses<Queue>> response) {
                MultipleResponses<Queue> responses = response.body();
                if (!responses.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, responses.getMessage(), responses.getData()));
                } else {
                    data.setValue(new Resource<>(Status.EMPTY, responses.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<MultipleResponses<Queue>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }

    public MutableLiveData<Resource<Integer>> countWaitingByMerchant() {
        final MutableLiveData<Resource<Integer>> data = new MutableLiveData<>();
        services.countWaitingQueueByMerchant(TOKEN).enqueue(new Callback<SingleResponse<Integer>>() {
            @Override
            public void onResponse(Call<SingleResponse<Integer>> call, Response<SingleResponse<Integer>> response) {
                SingleResponse<Integer> singleResponse = response.body();
                if (!singleResponse.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, singleResponse.getMessage(), singleResponse.getData()));
                } else {
                    data.setValue(new Resource<>(Status.EMPTY, singleResponse.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<Integer>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }

    public MutableLiveData<Resource<ArrayList<Queue>>> findWaitingByMerchant() {
        final MutableLiveData<Resource<ArrayList<Queue>>> data = new MutableLiveData<>();
        services.findWaitingQueueByMerchant(TOKEN).enqueue(new Callback<MultipleResponses<Queue>>() {
            @Override
            public void onResponse(Call<MultipleResponses<Queue>> call, Response<MultipleResponses<Queue>> response) {
                MultipleResponses<Queue> responses = response.body();
                if (!responses.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, responses.getMessage(), responses.getData()));
                } else {
                    data.setValue(new Resource<>(Status.EMPTY, responses.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<MultipleResponses<Queue>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }

    public LiveData<Resource<ArrayList<Queue>>> findHistoryByMerchant() {
        final MutableLiveData<Resource<ArrayList<Queue>>> data = new MutableLiveData<>();
        services.findHistoryQueueByMerchant(TOKEN).enqueue(new Callback<MultipleResponses<Queue>>() {
            @Override
            public void onResponse(Call<MultipleResponses<Queue>> call, Response<MultipleResponses<Queue>> response) {
                MultipleResponses<Queue> responses = response.body();
                if (!responses.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, responses.getMessage(), responses.getData()));
                } else {
                    data.setValue(new Resource<>(Status.EMPTY, responses.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<MultipleResponses<Queue>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }

    public LiveData<Resource<Queue>> updateStatus(Queue queue, QueueStatus queueStatus) {
        final MutableLiveData<Resource<Queue>> data = new MutableLiveData<>();
        services.updateQueueStatus(TOKEN, queue.getId(), queueStatus.getStatusInDatabase()).enqueue(new Callback<SingleResponse<Queue>>() {
            @Override
            public void onResponse(Call<SingleResponse<Queue>> call, Response<SingleResponse<Queue>> response) {
                SingleResponse<Queue> singleResponse = response.body();
                if (!singleResponse.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, singleResponse.getMessage(), singleResponse.getData()));
                } else {
                    data.setValue(new Resource<>(Status.ERROR, singleResponse.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<Queue>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }

    public LiveData<Resource<Queue>> findByQRCode(int id) {
        final MutableLiveData<Resource<Queue>> data = new MutableLiveData<>();
        services.findQueueByQRCode(TOKEN, id).enqueue(new Callback<SingleResponse<Queue>>() {
            @Override
            public void onResponse(Call<SingleResponse<Queue>> call, Response<SingleResponse<Queue>> response) {
                SingleResponse<Queue> singleResponse = response.body();
                if (!singleResponse.isError()) {
                    data.setValue(new Resource<>(Status.SUCCESS, singleResponse.getMessage(), singleResponse.getData()));
                } else {
                    data.setValue(new Resource<>(Status.ERROR, singleResponse.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<Queue>> call, Throwable t) {
                t.printStackTrace();
                data.setValue(new Resource<>(Status.ERROR, t.getMessage()));
            }
        });
        return data;
    }
}
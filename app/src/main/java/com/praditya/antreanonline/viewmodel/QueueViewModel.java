package com.praditya.antreanonline.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.praditya.antreanonline.model.EstimatedQueue;
import com.praditya.antreanonline.model.Queue;
import com.praditya.antreanonline.model.QueueStatus;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.model.Service;
import com.praditya.antreanonline.repository.QueueRepository;

import java.util.ArrayList;

public class QueueViewModel extends AndroidViewModel {

    private final QueueRepository queueRepository;
    private final MutableLiveData<String> date;
    private final MutableLiveData<QueueStatus> queueStatus;
    private final MutableLiveData<Integer> queueId;
    private Service service;
    private Queue queue;

    public QueueViewModel(@NonNull Application application) {
        super(application);
        queueRepository = new QueueRepository(application);
        date = new MutableLiveData<>();
        queueStatus = new MutableLiveData<>();
        queueId = new MutableLiveData<>();
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setDate(String date) {
        this.date.setValue(date);
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public void setQueueStatus(QueueStatus queueStatus) {
        this.queueStatus.setValue(queueStatus);
    }

    public void setQueueId(int id) {
        this.queueId.setValue(id);
    }

    public LiveData<Resource<EstimatedQueue>> findByDate() {
        return Transformations.switchMap(date, date ->
                queueRepository.findByDate(service.getId(), date));
    }

    public LiveData<Resource<Queue>> create(Queue queue) {
        return queueRepository.create(queue);
    }

    public LiveData<Resource<ArrayList<Queue>>> findActiveByUser() {
        return queueRepository.findActiveByUser();
    }

    public LiveData<Resource<ArrayList<Queue>>> findHistoryByUser() {
        return queueRepository.findHistoryByUser();
    }

    public LiveData<Resource<Integer>> countWaitingByMerchant() {
        return queueRepository.countWaitingByMerchant();
    }

    public LiveData<Resource<ArrayList<Queue>>> findWaitingByMerchant() {
        return queueRepository.findWaitingByMerchant();
    }

    public LiveData<Resource<ArrayList<Queue>>> findHistoryByMerchant() {
        return queueRepository.findHistoryByMerchant();
    }

    public LiveData<Resource<Queue>> updateStatus() {
        return Transformations.switchMap(queueStatus, queueStatus ->
                queueRepository.updateStatus(queue, queueStatus));
    }

    public LiveData<Resource<Queue>> findByQRCode() {
        return Transformations.switchMap(queueId, queueId ->
                queueRepository.findByQRCode(queueId));
    }
}

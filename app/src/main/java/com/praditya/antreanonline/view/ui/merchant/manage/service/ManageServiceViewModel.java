package com.praditya.antreanonline.view.ui.merchant.manage.service;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.model.Service;
import com.praditya.antreanonline.repository.ServiceRepository;

import java.util.ArrayList;

public class ManageServiceViewModel extends AndroidViewModel {

    private final ServiceRepository serviceRepository;

    public ManageServiceViewModel(@NonNull Application application) {
        super(application);
        serviceRepository = new ServiceRepository(application);
    }

    public LiveData<Resource<ArrayList<Service>>> findServiceByMerchant() {
        return serviceRepository.findServiceByMerchant();
    }

    public LiveData<Resource<Service>> create(Service service) {
        return serviceRepository.create(service);
    }

    public LiveData<Resource<Service>> update(Service service) {
        return serviceRepository.update(service);
    }
}

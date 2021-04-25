package com.praditya.antreanonline.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.praditya.antreanonline.model.BusinessHour;
import com.praditya.antreanonline.model.Category;
import com.praditya.antreanonline.model.Merchant;
import com.praditya.antreanonline.model.Queue;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.repository.MerchantRepository;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;

public class MerchantViewModel extends AndroidViewModel {

    private final MerchantRepository merchantRepository;
    private MutableLiveData<String> merchantName;
    private MutableLiveData<String> merchantCategory;

    public MerchantViewModel(@NonNull Application application) {
        super(application);
        merchantRepository = new MerchantRepository(application);
        merchantName = new MutableLiveData<>();
        merchantCategory = new MutableLiveData<>();
    }

    public void setMerchantName(String name) {
        this.merchantName.setValue(name);
    }

    public void setMerchantCategory(String category) {
        this.merchantCategory.setValue(category);
    }

    public LiveData<Resource<ArrayList<Merchant>>> findMerchantByName() {
        return Transformations.switchMap(merchantName, merchantName ->
                merchantRepository.findByName(merchantName));
    }

    public LiveData<Resource<ArrayList<Merchant>>> findMerchantByCategory() {
        return Transformations.switchMap(merchantCategory, merchantCategory ->
                merchantRepository.findByCategory(merchantCategory));
    }

    public LiveData<Resource<Merchant>> findByUser() {
        return merchantRepository.findByUser();
    }

    public LiveData<Resource<Merchant>> findById(int merchantId) {
        return merchantRepository.findById(merchantId);
    }

    public LiveData<Resource<Merchant>> create(Merchant merchant) {
        return merchantRepository.create(merchant);
    }

    public LiveData<Resource<Merchant>> update(Merchant merchant) {
        return merchantRepository.update(merchant);
    }

    public LiveData<Resource<Merchant>> setBusinessHours(Merchant merchant) {
        return merchantRepository.setBusinessHours(merchant);
    }
}

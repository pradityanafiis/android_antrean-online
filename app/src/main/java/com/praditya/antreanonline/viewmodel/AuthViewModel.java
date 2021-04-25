package com.praditya.antreanonline.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.model.User;
import com.praditya.antreanonline.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    public LiveData<Resource<User>> updateProfile(User user) {
        return authRepository.update(user);
    }

    public LiveData<Resource<User>> changePassword(String currentPassword, String newPassword) {
        return authRepository.changePassword(currentPassword, newPassword);
    }
}

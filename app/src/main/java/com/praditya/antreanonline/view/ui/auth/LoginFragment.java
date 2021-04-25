package com.praditya.antreanonline.view.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.api.ApiClient;
import com.praditya.antreanonline.api.Services;
import com.praditya.antreanonline.api.response.SingleResponse;
import com.praditya.antreanonline.model.User;
import com.praditya.antreanonline.storage.SharedPreferencesManager;
import com.praditya.antreanonline.view.ui.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private Services services = ApiClient.getServices();
    private String firebaseToken;
    @BindView(R.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R.id.et_email)
    EditText etEmail;
    @OnTextChanged(R.id.et_email)
    void emailOnTextChanged() {
        tilEmail.setError(null);
    }
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.et_password)
    EditText etPassword;
    @OnTextChanged(R.id.et_password)
    void passwordOnTextChanged() {
        tilPassword.setError(null);
    }
    @BindView(R.id.btn_login)
    Button btnLogin;
    @OnClick(R.id.btn_login)
    void login() {
        setButtonDisabled();
        boolean ready = true;
        String email = etEmail.getText().toString().trim().toLowerCase();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            ready = false;
            tilEmail.setError("Kolom alamat surel tidak boleh kosong!");
        }

        if (password.isEmpty()) {
            ready = false;
            tilPassword.setError("Kolom kata sandi tidak boleh kosong!");
        }

        if (ready) {
            login(email, password);
        } else {
            setButtonEnabled();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    showMessage(task.getException().toString());
                } else {
                    firebaseToken = task.getResult().getToken();
                }
            }
        });
        return view;
    }

    public void login(String email, String password) {
        services.login(email, password, firebaseToken).enqueue(new Callback<SingleResponse<User>>() {
            @Override
            public void onResponse(Call<SingleResponse<User>> call, Response<SingleResponse<User>> response) {
                boolean error = response.body().isError();
                if (!error) {
                    saveUser(response.body().getData());
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    showMessage(response.body().getMessage());
                    setButtonEnabled();
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<User>> call, Throwable t) {
                t.printStackTrace();
                showMessage(t.getMessage());
                setButtonEnabled();
            }
        });
    }

    public void setButtonEnabled() {
        btnLogin.setText("Masuk");
        btnLogin.setEnabled(true);
    }

    public void setButtonDisabled() {
        closeKeyboard();
        tilEmail.setError(null);
        tilPassword.setError(null);
        btnLogin.setText("Memuat...");
        btnLogin.setEnabled(false);
    }

    public void saveUser(User user) {
        SharedPreferencesManager.getSharedPreferencesManager(getContext()).saveUser(user);
    }

    private void closeKeyboard() {
        View view = this.getView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showMessage(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
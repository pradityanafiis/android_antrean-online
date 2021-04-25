package com.praditya.antreanonline.view.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.api.ApiClient;
import com.praditya.antreanonline.api.Services;
import com.praditya.antreanonline.api.response.SingleResponse;
import com.praditya.antreanonline.model.User;
import com.praditya.antreanonline.storage.SharedPreferencesManager;
import com.praditya.antreanonline.view.ui.MainActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private final String TAG = getTag();
    private Services services = ApiClient.getServices();

    @BindViews({R.id.til_nik, R.id.til_name, R.id.til_phone, R.id.til_email, R.id.til_password, R.id.til_password_confirm})
    List<TextInputLayout> textInputLayouts;
    @BindViews({R.id.et_nik, R.id.et_name, R.id.et_phone, R.id.et_email, R.id.et_password, R.id.et_password_confirm})
    List<EditText> editTexts;

    @BindView(R.id.til_nik)
    TextInputLayout tilNIK;
    @BindView(R.id.et_nik)
    EditText etNIK;
    @OnTextChanged(R.id.et_nik)
    void nikOnTextChange() {
        tilNIK.setError(null);
    }

    @BindView(R.id.til_name)
    TextInputLayout tilName;
    @BindView(R.id.et_name)
    EditText etName;
    @OnTextChanged(R.id.et_name)
    void nameOnTextChange() {
        tilName.setError(null);
    }

    @BindView(R.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R.id.et_email)
    EditText etEmail;
    @OnTextChanged(R.id.et_email)
    void emailOnTextChange() {
        tilEmail.setError(null);
    }

    @BindView(R.id.til_phone)
    TextInputLayout tilPhone;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @OnTextChanged(R.id.et_phone)
    void phoneOnTextChange() {
        tilPhone.setError(null);
    }

    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.et_password)
    EditText etPassword;
    @OnTextChanged(R.id.et_password)
    void passwordOnTextChange() {
        tilPassword.setError(null);
    }

    @BindView(R.id.til_password_confirm)
    TextInputLayout tilPasswordConfirm;
    @BindView(R.id.et_password_confirm)
    EditText etPasswordConfirm;
    @OnTextChanged(R.id.et_password_confirm)
    void passwordConfirmOnTextChange() {
        tilPasswordConfirm.setError(null);
    }

    @BindView(R.id.btn_register)
    Button btnRegister;
    @OnClick(R.id.btn_register)
    void register() {
        setButtonDisabled();
        boolean ready = true;
        String identityNumber = etNIK.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String passwordConfirm = etPasswordConfirm.getText().toString().trim();

        Log.d(TAG, "register: " + phone);

        if (identityNumber.isEmpty()) {
            ready = false;
            tilNIK.setError("Kolom NIK tidak boleh kosong!");
        }

        if (identityNumber.length() != 16) {
            ready = false;
            tilNIK.setError("Kolom NIK harus berisi tepat 16 digit angka!");
        }

        if (name.isEmpty()) {
            ready = false;
            tilName.setError("Kolom nama tidak boleh kosong!");
        }

        if (phone.isEmpty()) {
            ready = false;
            tilPhone.setError("Kolom telepon seluler tidak boleh kosong!");
        } else {
            if (phone.charAt(0) != '8') {
                ready = false;
                tilPhone.setError("Kolom telepon seluler harus diawali angka 8!");
            }
        }

        if (email.isEmpty()) {
            ready = false;
            tilEmail.setError("Kolom email tidak boleh kosong!");
        }

        if (password.isEmpty()) {
            ready = false;
            tilPassword.setError("Kolom kata sandi tidak boleh kosong!");
        }

        if (password.length() < 8) {
            ready = false;
            tilPassword.setError("Kata sandi minimal 8 karakter!");
        }

        if (passwordConfirm.isEmpty()) {
            ready = false;
            tilPasswordConfirm.setError("Kolom konfirmasi kata sandi tidak boleh kosong!");
        }

        if (!password.equals(passwordConfirm)) {
            ready = false;
            tilPassword.setError("Kolom kata sandi dan konfirmasi kata sandi harus sama!");
            tilPasswordConfirm.setError("Kolom kata sandi dan konfirmasi kata sandi harus sama!");
        }

        if (ready) {
            User user = new User(identityNumber, name, phone, email, password);
            register(user);
        } else {
            setButtonEnabled();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void register(User user) {
        services.register(user).enqueue(new Callback<SingleResponse<User>>() {
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
        btnRegister.setText("Daftar");
        btnRegister.setEnabled(true);
    }

    public void setButtonDisabled() {
        closeKeyboard();
        clearError();
        btnRegister.setText("Memuat...");
        btnRegister.setEnabled(false);
    }

    private void clearError() {
        for (TextInputLayout textInputLayout: textInputLayouts) {
            textInputLayout.setError(null);
        }
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
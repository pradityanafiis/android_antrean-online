package com.praditya.antreanonline.view.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.model.User;
import com.praditya.antreanonline.viewmodel.AuthViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class ChangePasswordActivity extends AppCompatActivity {

    private AuthViewModel viewModel;
    @BindView(R.id.app_bar)
    Toolbar toolbar;
    @BindView(R.id.layout_main)
    LinearLayout layoutMain;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.til_current_password)
    TextInputLayout tilCurrentPassword;
    @BindView(R.id.et_current_password)
    TextInputEditText etCurrentPassword;
    @OnTextChanged(R.id.et_current_password)
    void currentPasswordOnTextChange() {
        tilCurrentPassword.setError(null);
    }
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.et_password)
    TextInputEditText etPassword;
    @OnTextChanged(R.id.et_password)
    void passwordOnTextChange() {
        tilPassword.setError(null);
    }
    @BindView(R.id.til_password_confirm)
    TextInputLayout tilPasswordConfirm;
    @BindView(R.id.et_password_confirm)
    TextInputEditText etPasswordConfirm;
    @OnTextChanged(R.id.et_password_confirm)
    void passwordConfirmOnTextChange() {
        tilPasswordConfirm.setError(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        viewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(AuthViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.finish_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_finish:
                onFinishClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onFinishClicked() {
        showLoading(true);
        boolean ready = true;
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etPassword.getText().toString().trim();
        String newPasswordConfirm = etPasswordConfirm.getText().toString().trim();

        if (currentPassword.isEmpty()) {
            ready = false;
            tilCurrentPassword.setError("Kolom kata sandi saat ini tidak boleh kosong!");
        }

        if (newPassword.isEmpty()) {
            ready = false;
            tilPassword.setError("Kolom kata sandi baru tidak boleh kosong!");
        }

        if (newPassword.length() < 8) {
            ready = false;
            tilPassword.setError("Kata sandi minimal 8 karakter!");
        }

        if (newPasswordConfirm.isEmpty()) {
            ready = false;
            tilPasswordConfirm.setError("Kolom konfirmasi kata sandi baru tidak boleh kosong!");
        }

        if (!newPassword.equals(newPasswordConfirm)) {
            ready = false;
            tilPassword.setError("Kolom kata sandi baru dan konfirmasi kata sandi harus sama!");
            tilPasswordConfirm.setError("Kolom kata sandi baru dan konfirmasi kata sandi harus sama!");
        }

        if (ready) {
            changePassword(currentPassword, newPassword);
        } else {
            showLoading(false);
        }
    }

    private void changePassword(String currentPassword, String newPassword) {
        viewModel.changePassword(currentPassword, newPassword).observe(ChangePasswordActivity.this, new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> userResource) {
                switch (userResource.getStatus()) {
                    case SUCCESS:
                        etCurrentPassword.getText().clear();
                        etPassword.getText().clear();
                        etPasswordConfirm.getText().clear();
                        showMessage(userResource.getMessage());
                        break;
                    case ERROR:
                    case EMPTY:
                        showMessage(userResource.getMessage());
                        break;
                }
                showLoading(false);
            }
        });
    }

    private void showLoading(boolean state) {
        if (state) {
            loading.setVisibility(View.VISIBLE);
            layoutMain.setVisibility(View.GONE);
        } else {
            loading.setVisibility(View.GONE);
            layoutMain.setVisibility(View.VISIBLE);
        }
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
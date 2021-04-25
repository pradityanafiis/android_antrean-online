package com.praditya.antreanonline.view.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.api.ApiClient;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.model.User;
import com.praditya.antreanonline.storage.SharedPreferencesManager;
import com.praditya.antreanonline.viewmodel.AuthViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditAccountActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private final int REQUEST_CHOOSE_IMAGE = 1;
    private User user;
    private AuthViewModel viewModel;
    private String currentPhotoPath;
    private Bitmap bitmap;
    @BindView(R.id.app_bar)
    Toolbar toolbar;
    @BindView(R.id.layout_main)
    LinearLayout layoutMain;
    @BindView(R.id.til_name)
    TextInputLayout tilName;
    @BindView(R.id.et_name)
    TextInputEditText etName;
    @BindView(R.id.til_phone)
    TextInputLayout tilPhone;
    @BindView(R.id.et_phone)
    TextInputEditText etPhone;
    @BindView(R.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R.id.et_email)
    TextInputEditText etEmail;
    @BindView(R.id.iv_profile_photo)
    CircleImageView imageView;
    @OnClick(R.id.tv_change_photo)
    void changePhoto() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_CHOOSE_IMAGE);
    }
    @BindView(R.id.loading)
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        user = SharedPreferencesManager.getSharedPreferencesManager(this).getUser();
        initView(user);
        viewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(AuthViewModel.class);
    }

    private void initView(User user) {
        etName.setText(user.getName());
        etPhone.setText(user.getPhone());
        etEmail.setText(user.getEmail());
        if (user.getPhoto() != null) {
            Glide.with(this).load(decodeImage(user.getPhoto())).centerCrop().into(imageView);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CHOOSE_IMAGE:
                    Uri selectedImage = data.getData();
                    bitmap = BitmapFactory.decodeFile(getRealPathFromURI(selectedImage));
                    Glide.with(this).load(BitmapFactory.decodeFile(getRealPathFromURI(selectedImage))).centerCrop().into(imageView);
                    break;
            }
        }
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
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (name.isEmpty()) {
            ready = false;
            tilName.setError("Kolom nama lengkap tidak boleh kosong!");
        }

        if (email.isEmpty()) {
            ready = false;
            tilEmail.setError("Kolom alamat surel tidak boleh kosong!");
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

        if (ready) {
            updateUser(new User(name, email, phone));
        } else {
            showLoading(false);
        }
    }

    private void updateUser(User user) {
        if (bitmap != null) {
            user.setPhoto(encodeImage(bitmap));
        }
        viewModel.updateProfile(user).observe(EditAccountActivity.this, new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> userResource) {
                switch (userResource.getStatus()) {
                    case SUCCESS:
                        SharedPreferencesManager.getSharedPreferencesManager(EditAccountActivity.this).updateUser(userResource.getData());
                        initView(userResource.getData());
                        showMessage(userResource.getMessage());
                        break;
                    case EMPTY:
                    case ERROR:
                        showMessage(userResource.getMessage());
                        break;
                }
                showLoading(false);
            }
        });
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private Bitmap decodeImage(String image) {
        byte[] imageBytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
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
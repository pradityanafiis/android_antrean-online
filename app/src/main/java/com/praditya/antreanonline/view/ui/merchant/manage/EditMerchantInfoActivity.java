package com.praditya.antreanonline.view.ui.merchant.manage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.content.CursorLoader;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.Merchant;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.view.ui.MapsActivity;
import com.praditya.antreanonline.viewmodel.MerchantViewModel;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditMerchantInfoActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private final int OPEN_MAP_REQUEST_CODE = 1;
    private final int CHOOSE_IMAGE_REQUEST_CODE = 2;
    private Merchant merchant;
    private MerchantViewModel viewModel;
    private LatLng latLng;
    private Bitmap bitmap;
    @BindView(R.id.layout_main)
    LinearLayout layoutMain;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.app_bar)
    Toolbar toolbar;
    @BindView(R.id.iv_merchant_photo)
    CircleImageView imageView;
    @OnClick(R.id.tv_change_photo)
    void changePhoto() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, CHOOSE_IMAGE_REQUEST_CODE);
    }
    @BindView(R.id.til_name)
    TextInputLayout tilName;
    @BindView(R.id.et_name)
    TextInputEditText etName;
    @BindView(R.id.til_address)
    TextInputLayout tilAddress;
    @BindView(R.id.et_address)
    TextInputEditText etAddress;
    @BindView(R.id.til_phone)
    TextInputLayout tilPhone;
    @BindView(R.id.et_phone)
    TextInputEditText etPhone;
    @OnClick(R.id.btn_open_map)
    void openMap() {
        Intent intent = new Intent(EditMerchantInfoActivity.this, MapsActivity.class);
        startActivityForResult(intent, OPEN_MAP_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_merchant_info);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(MerchantViewModel.class);
        getMerchant();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case OPEN_MAP_REQUEST_CODE:
                    Bundle bundle = data.getExtras();
                    etAddress.setText(bundle.getString("address"));
                    latLng = new LatLng(bundle.getDouble("latitude"), bundle.getDouble("longitude"));
                    break;
                case CHOOSE_IMAGE_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    bitmap = BitmapFactory.decodeFile(getRealPathFromURI(selectedImage));
                    Glide.with(this).load(bitmap).centerCrop().into(imageView);
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

    private void initView(Merchant merchant) {
        this.merchant = merchant;
        etName.setText(merchant.getName());
        etPhone.setText(merchant.getPhone());
        etAddress.setText(merchant.getAddress());
        if (merchant.getPhoto() != null) {
            Glide.with(this).load(decodeImage(merchant.getPhoto())).centerCrop().into(imageView);
        }
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

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(EditMerchantInfoActivity.this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void getMerchant() {
        showLoading(true);
        viewModel.findByUser().observe(EditMerchantInfoActivity.this, new Observer<Resource<Merchant>>() {
            @Override
            public void onChanged(Resource<Merchant> merchantResource) {
                switch (merchantResource.getStatus()) {
                    case SUCCESS:
                        initView(merchantResource.getData());
                        showLoading(false);
                        break;
                    case ERROR:
                    case EMPTY:
                        showLoading(false);
                        break;
                }
            }
        });
    }

    private void onFinishClicked() {
        showLoading(true);
        boolean ready = true;
        String name = etName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (name.isEmpty()) {
            ready = false;
            tilName.setError("Kolom nama bisnis tidak boleh kosong!");
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

        if (address.isEmpty()) {
            ready = false;
            tilAddress.setError("Kolom alamat tidak boleh kosong!");
        }

        if (ready) {
            if (latLng != null) {
                merchant.setAddress(address);
                merchant.setLatitude(latLng.latitude);
                merchant.setLongitude(latLng.longitude);
            }

            if (bitmap != null) {
                merchant.setPhoto(encodeImage(bitmap));
            }

            merchant.setName(name);
            merchant.setPhone(phone);
            updateMerchant(merchant);
        }
    }

    private void updateMerchant(Merchant merchant) {
        viewModel.update(merchant).observe(EditMerchantInfoActivity.this, new Observer<Resource<Merchant>>() {
            @Override
            public void onChanged(Resource<Merchant> merchantResource) {
                switch (merchantResource.getStatus()) {
                    case SUCCESS:
                    case EMPTY:
                    case ERROR:
                        showMessage(merchantResource.getMessage());
                        break;
                }
                showLoading(false);
            }
        });
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private void showLoading(boolean state) {
        if (state) {
            layoutMain.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
        } else {
            layoutMain.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
        }
    }
}
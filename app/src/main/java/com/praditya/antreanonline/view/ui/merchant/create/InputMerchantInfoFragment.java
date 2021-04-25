package com.praditya.antreanonline.view.ui.merchant.create;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.Merchant;
import com.praditya.antreanonline.view.ui.MapsActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class InputMerchantInfoFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private final int OPEN_MAP_REQUEST_CODE = 1;
    private final int REQUEST_CHOOSE_IMAGE = 2;
    private LatLng latLng;
    private Bitmap bitmap;
    @BindView(R.id.iv_merchant_photo)
    CircleImageView imageView;
    @BindView(R.id.tv_change_photo)
    MaterialTextView tvChangePhoto;
    @OnClick(R.id.tv_change_photo)
    void changePhoto() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_CHOOSE_IMAGE);
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
        Intent intent = new Intent(this.getActivity(), MapsActivity.class);
        startActivityForResult(intent, OPEN_MAP_REQUEST_CODE);
    }
    @BindView(R.id.btn_finish_merchant_info)
    MaterialButton btnFinish;
    @OnClick(R.id.btn_finish_merchant_info)
    void finish() {
        setButtonEnabled(false);
        boolean ready = true;
        String name = etName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (name.isEmpty()) {
            ready = false;
            tilName.setError("Kolom nama bisnis tidak boleh kosong!");
        }

        if (address.isEmpty()) {
            ready = false;
            tilAddress.setError("Kolom alamat tidak boleh kosong!");
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

        if (latLng == null) {
            ready = false;
            tilAddress.setError("Anda harus menentukan alamat menggunakan maps!");
        }

        if (bitmap == null) {
            ready = false;
            showMessage("Anda harus memilih gambar!");
        }

        if (ready) {
            Merchant merchant = new Merchant(name, address, latLng.latitude, latLng.longitude, phone);
            merchant.setPhoto(encodeImage(bitmap));
            Bundle bundle = new Bundle();
            bundle.putSerializable("merchant", merchant);
            SelectMerchantCategoryFragment fragment = new SelectMerchantCategoryFragment();
            fragment.setArguments(bundle);
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack("Input Merchant Info")
                    .commit();
        } else {
            setButtonEnabled(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_merchant_info, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Informasi Bisnis");
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case OPEN_MAP_REQUEST_CODE:
                    Bundle bundle = data.getExtras();
                    etAddress.setText(bundle.getString("address"));
                    latLng = new LatLng(bundle.getDouble("latitude"), bundle.getDouble("longitude"));
                    break;
                case REQUEST_CHOOSE_IMAGE:
                    Uri selectedImage = data.getData();
                    bitmap = BitmapFactory.decodeFile(getRealPathFromURI(selectedImage));
                    Glide.with(this).load(bitmap).centerCrop().into(imageView);
            }
        }
    }

    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void setButtonEnabled(boolean state) {
        if (state) {
            btnFinish.setText("Selanjutnya");
            btnFinish.setEnabled(true);
        } else {
            btnFinish.setText("Memuat...");
            btnFinish.setEnabled(false);
        }
    }

    private void showMessage(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
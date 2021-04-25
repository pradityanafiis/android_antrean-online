package com.praditya.antreanonline.view.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.praditya.antreanonline.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//FragmentActivity seharusnya
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener {

    private final String TAG = getClass().getSimpleName();
    private GoogleMap map;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;
    private Marker marker;
    private LatLng currentLatLng;
    private Geocoder geocoder;
    private String address;

    @BindView(R.id.tv_merchant_address)
    MaterialTextView tvAddress;
    @BindView(R.id.btn_choose_location)
    MaterialButton btnChooseLocation;
    @OnClick(R.id.btn_choose_location)
    void chooseLocation() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("address", address);
        bundle.putDouble("latitude", currentLatLng.latitude);
        bundle.putDouble("longitude", currentLatLng.longitude);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        enableMyLocation();
        map.setOnCameraMoveListener(this::onCameraMove);
        map.setOnCameraIdleListener(this::onCameraIdle);

        currentLatLng = map.getCameraPosition().target;
        marker = map.addMarker(new MarkerOptions().position(map.getCameraPosition().target));
    }

    @Override
    public void onCameraMove() {
        currentLatLng = map.getCameraPosition().target;
        marker.setPosition(currentLatLng);
        showAddress(false);
    }

    @Override
    public void onCameraIdle() {
        try {
            List<Address> addresses = geocoder.getFromLocation(currentLatLng.latitude, currentLatLng.longitude, 1);
            for (Address address: addresses) {
                this.address = address.getAddressLine(0);
            }
            showAddress(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                map.setMyLocationEnabled(true);
                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));
                Location location = locationManager.getLastKnownLocation(bestProvider);
                LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 18));
            }
        } else {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            enableMyLocation();
        } else {
            permissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog.newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    private void chooseLocationButtonEnabled(boolean state) {
        if (state)
            btnChooseLocation.setEnabled(true);
        else
            btnChooseLocation.setEnabled(false);
    }

    private void showAddress(boolean state) {
        if (state) {
            tvAddress.setText(address);
            tvAddress.setVisibility(View.VISIBLE);
            chooseLocationButtonEnabled(true);
        } else {
            tvAddress.setVisibility(View.INVISIBLE);
            chooseLocationButtonEnabled(false);
        }
    }
}
package com.praditya.antreanonline.view.ui.merchant.manage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;
import com.praditya.antreanonline.model.Queue;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.viewmodel.QueueViewModel;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCodeReaderActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private final String TAG = getClass().getSimpleName();
    private ZXingScannerView scannerView;
    private QueueViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        viewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(QueueViewModel.class);
        viewModel.findByQRCode().observe(QRCodeReaderActivity.this, new Observer<Resource<Queue>>() {
            @Override
            public void onChanged(Resource<Queue> queueResource) {
                switch (queueResource.getStatus()) {
                    case SUCCESS:
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("queue", queueResource.getData());
                        Intent intent = new Intent(QRCodeReaderActivity.this, ShowQueueActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                        break;
                    case EMPTY:
                    case ERROR:
                        showMessage(queueResource.getMessage());
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this::handleResult);
        scannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        int id;
        try {
            id = Integer.parseInt(rawResult.getText());
            viewModel.setQueueId(id);
        } catch (Exception e) {
            showMessage("Tidak dapat mengenali QR code!");
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scannerView.resumeCameraPreview(QRCodeReaderActivity.this::handleResult);
            }
        }, 2000);
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
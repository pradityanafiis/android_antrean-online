package com.praditya.antreanonline.view.ui.merchant.manage.service;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.Service;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageServiceDialog extends AppCompatDialogFragment {

    @BindView(R.id.et_service_name)
    TextInputEditText etServiceName;
    @BindView(R.id.et_service_description)
    TextInputEditText etServiceDescription;
    @BindView(R.id.et_quota)
    TextInputEditText etQuota;
    @BindView(R.id.et_interval)
    TextInputEditText etInterval;
    @BindView(R.id.et_max_day)
    TextInputEditText etMaxDay;
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private String title;
    private boolean isEditing;
    private Service service;
    private ServiceDialogListener listener;

    public ManageServiceDialog(Context context, String title, boolean isEditing) {
        this.context = context;
        this.title = title;
        this.isEditing = isEditing;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setListener(ServiceDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_service, null);
        builder.setView(view);
        builder.setTitle(title);
        builder.setNegativeButton("batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean ready = true;
                String name = etServiceName.getText().toString().trim();
                String description = etServiceDescription.getText().toString().trim();
                String quotaString = etQuota.getText().toString().trim();
                int quota = Integer.parseInt(quotaString);
                String intervalString = etInterval.getText().toString().trim();
                int interval = Integer.parseInt(intervalString);
                String maxDayString = etMaxDay.getText().toString().trim();
                int maxDay = Integer.parseInt(maxDayString);

                if (name.isEmpty()) {
                    ready = false;
                    showMessage("Kolom nama layanan tidak boleh kosong!");
                }

                if (description.isEmpty()) {
                    ready = false;
                    showMessage("Kolom deskripsi layanan tidak boleh kosong!");
                }

                if (quotaString.isEmpty()) {
                    ready = false;
                    showMessage("Kolom kuota antrean tidak boleh kosong!");
                }

                if (intervalString.isEmpty()) {
                    ready = false;
                    showMessage("Kolom jarak antrean tidak boleh kosong!");
                }

                if (maxDayString.isEmpty()) {
                    ready = false;
                    showMessage("Kolom maksimal hari pengambilan tidak boleh kosong!");
                }

                if (ready) {
                    if (isEditing) {
                        service.setName(name);
                        service.setDescription(description);
                        service.setQuota(quota);
                        service.setInterval(interval);
                        service.setMaxScheduledDay(maxDay);
                        listener.updateService(service);
                    } else {
                        service = new Service(name, description, quota, interval, maxDay);
                        listener.createService(service);
                    }
                }
            }
        });
        ButterKnife.bind(this, view);
        initDialog();
        return builder.create();
    }

    private void initDialog() {
        if (isEditing) {
            etServiceName.setText(service.getName());
            etServiceDescription.setText(service.getDescription());
            etQuota.setText(String.valueOf(service.getQuota()));
            etInterval.setText(String.valueOf(service.getInterval()));
            etMaxDay.setText(String.valueOf(service.getMaxScheduledDay()));
        }
    }

    public interface ServiceDialogListener {
        void createService(Service service);
        void updateService(Service service);
    }

    private void showMessage(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}

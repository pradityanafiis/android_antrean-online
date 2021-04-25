package com.praditya.antreanonline.view.ui.merchant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.BusinessHour;
import com.praditya.antreanonline.model.Day;
import com.praditya.antreanonline.model.EstimatedQueue;
import com.praditya.antreanonline.model.Merchant;
import com.praditya.antreanonline.model.Queue;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.model.Service;
import com.praditya.antreanonline.view.dialog.DatePickerDialog;
import com.praditya.antreanonline.view.ui.HomeFragment;
import com.praditya.antreanonline.view.ui.MainActivity;
import com.praditya.antreanonline.view.ui.QueueFragment;
import com.praditya.antreanonline.viewmodel.QueueViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseDateActivity extends AppCompatActivity implements android.app.DatePickerDialog.OnDateSetListener {

    private final String TAG = getClass().getSimpleName();
    private final SimpleDateFormat timeFormatWithSecond = new SimpleDateFormat("HH:mm:ss");
    private QueueViewModel queueViewModel;
    private Merchant merchant;
    private Service service;
    private BusinessHour businessHour;
    private Queue queue = new Queue();
    @BindColor(R.color.colorGreen)
    int green;
    @BindColor(R.color.colorRed)
    int red;
    @BindView(R.id.app_bar)
    Toolbar toolbar;
    @BindView(R.id.tv_merchant_name)
    TextView tvMerchantName;
    @BindView(R.id.tv_service_name)
    TextView tvServiceName;
    @BindView(R.id.cv_queue_info)
    MaterialCardView cvQueueInfo;
    @BindView(R.id.tv_queue_status)
    MaterialTextView tvQueueStatus;
    @BindView(R.id.tv_queue_number)
    MaterialTextView tvQueueNumber;
    @BindView(R.id.tv_queue_quota)
    MaterialTextView tvQueueQuota;
    @BindView(R.id.tv_estimated_time_serve)
    MaterialTextView tvEstimatedTimeServe;
    @BindView(R.id.btn_process_queue)
    MaterialButton btnProcessQueue;
    @OnClick(R.id.btn_process_queue)
    void createQueue() {
        queue.setServiceId(service.getId());
        createQueue(queue);
    }
    @BindView(R.id.btn_select_date)
    MaterialButton btnSelecDate;
    @OnClick(R.id.btn_select_date)
    void openCalendar() {
        DialogFragment dialogFragment = new DatePickerDialog(service.getMaxScheduledDay());
        dialogFragment.show(getSupportFragmentManager(), "Date Picker");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initView();
        initViewModel();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String selectedDate = getDateToString(datePicker);
        String selectedDay = getDayFromDatePicker(datePicker);
        if (merchant.isOpen(selectedDay)) {
            businessHour = merchant.getTodayBusinessHour(selectedDay);
            if (selectedDay.equalsIgnoreCase(getDayToday()) && isAlreadyClose()) {
                showQueueInfo(false);
                showCreateQueueButton(false);
                showMessage("Waktu operasional sudah selesai, pilih hari lain!");
            } else {
                queueViewModel.setDate(selectedDate);
                queue.setSchedule(selectedDate);
            }
        } else {
            showQueueInfo(false);
            showCreateQueueButton(false);
            showMessage("Tutup pada hari " + convertDayToIndonesia(selectedDay) + ", pilih hari lain!");
        }
        displaySelectedDate(datePicker);
    }

    private void initView() {
        merchant = (Merchant) getIntent().getSerializableExtra("merchant");
        service = (Service) getIntent().getSerializableExtra("service");
        tvMerchantName.setText(merchant.getName());
        tvServiceName.setText(service.getName());
        showQueueInfo(false);
        showCreateQueueButton(false);
    }

    private void initViewModel() {
        queueViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(QueueViewModel.class);
        queueViewModel.setService(service);
        queueViewModel.findByDate().observe(ChooseDateActivity.this, new Observer<Resource<EstimatedQueue>>() {
            @Override
            public void onChanged(Resource<EstimatedQueue> estimatedQueueResource) {
                switch (estimatedQueueResource.getStatus()) {
                    case SUCCESS:
                        setQueueInfo(true, estimatedQueueResource.getData());
                        break;
                    case EMPTY:
                        setQueueInfo(false, estimatedQueueResource.getData());
                        break;
                    case ERROR:
                        showMessage(estimatedQueueResource.getMessage());
                        break;
                }
            }
        });
    }

    private void createQueue(Queue queue) {
        queueViewModel.create(queue).observe(ChooseDateActivity.this, new Observer<Resource<Queue>>() {
            @Override
            public void onChanged(Resource<Queue> queueResource) {
                switch (queueResource.getStatus()) {
                    case SUCCESS:
                        Intent intent = new Intent(ChooseDateActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("active", "queue");
                        startActivity(intent);
                        break;
                    case ERROR:
                    case EMPTY:
                        showMessage(queueResource.getMessage());
                        break;
                }
            }
        });
    }

    private String getDateToString(DatePicker datePicker) {
        return datePicker.getYear() + "-" + (datePicker.getMonth()+1) + "-" + datePicker.getDayOfMonth();
    }

    private String getDayFromDatePicker(DatePicker datePicker) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(calendar.getTime());
    }

    private String convertDayToIndonesia(String dayInEnglish) {
        String dayInIndonesia = null;
        for (Day day: Day.values()) {
            if (day.getDayInEnglish().equals(dayInEnglish)) {
                dayInIndonesia = day.getDayInIndonesia();
            }
        }
        return dayInIndonesia;
    }

    private void displaySelectedDate(DatePicker datePicker) {
        btnSelecDate.setText(convertDayToIndonesia(getDayFromDatePicker(datePicker)) + ", " + datePicker.getDayOfMonth() + "-" + (datePicker.getMonth()+1) + "-" + datePicker.getYear());
    }

    private String getDayToday() {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(today);
    }

    private Date getCloseTime() {
        Date closeTime = null;
        try {
            closeTime = timeFormatWithSecond.parse(businessHour.getCloseTime());
        } catch (ParseException e) {
            e.printStackTrace();
            showMessage(e.getMessage());
        }
        return closeTime;
    }

    private Date getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        String currentTimeString = timeFormatWithSecond.format(calendar.getTime());
        Date currentTime = null;
        try {
            currentTime = timeFormatWithSecond.parse(currentTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
            showMessage(e.getMessage());
        }
        return currentTime;
    }

    private boolean isAlreadyClose() {
        return getCurrentTime().compareTo(getCloseTime()) > 0;
    }

    private void setQueueInfo(boolean isQueueAvailable, EstimatedQueue estimatedQueue) {
        if (isQueueAvailable) {
            tvQueueStatus.setText("Tersedia");
            tvQueueStatus.setTextColor(green);
            tvEstimatedTimeServe.setText("Pukul " + estimatedQueue.getEstimatedTimeServe());
            showCreateQueueButton(true);
            createQueueButtonEnabled(true);
            queue.setEstimatedTimeServe(estimatedQueue.getEstimatedTimeServe());
        } else {
            tvQueueStatus.setText("Tidak Tersedia");
            tvQueueStatus.setTextColor(red);
            tvEstimatedTimeServe.setText("-");
            showCreateQueueButton(false);
        }
        tvQueueNumber.setText(estimatedQueue.getQueueNumber() + " antrean");
        tvQueueQuota.setText(service.getQuota() - estimatedQueue.getQueueNumber() + " antrean");
        showQueueInfo(true);
    }

    private void showQueueInfo(boolean state) {
        if (state)
            cvQueueInfo.setVisibility(View.VISIBLE);
        else
            cvQueueInfo.setVisibility(View.GONE);
    }

    private void createQueueButtonEnabled(boolean state) {
        if (state)
            btnProcessQueue.setEnabled(true);
        else
            btnProcessQueue.setEnabled(false);
    }

    private void showCreateQueueButton(boolean state) {
        if (state)
            btnProcessQueue.setVisibility(View.VISIBLE);
        else
            btnProcessQueue.setVisibility(View.INVISIBLE);
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
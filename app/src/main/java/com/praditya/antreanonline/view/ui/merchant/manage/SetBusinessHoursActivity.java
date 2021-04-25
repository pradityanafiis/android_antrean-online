package com.praditya.antreanonline.view.ui.merchant.manage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.BusinessHour;
import com.praditya.antreanonline.model.Day;
import com.praditya.antreanonline.model.Merchant;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.view.dialog.TimePickerDialog;
import com.praditya.antreanonline.viewmodel.MerchantViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetBusinessHoursActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private ArrayList<BusinessHour> businessHours;
    private TimePickerDialog timePickerDialog = new TimePickerDialog();
    private String openTime = null;
    private String closeTime = null;
    private MerchantViewModel viewModel;
    @BindView(R.id.app_bar)
    Toolbar toolbar;
    @BindView(R.id.layout_main)
    LinearLayout layoutMain;
    @BindView(R.id.btn_select_open)
    MaterialButton btnSelectOpen;
    @OnClick(R.id.btn_select_open)
    void pickOpenTime() {
        timePickerDialog.setOnTimeSetListener(new android.app.TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                openTime = convertTime(getTimeFromTimePicker(timePicker));
                btnSelectOpen.setText("Pukul " + openTime);
            }
        });
        timePickerDialog.show(getSupportFragmentManager(), "Open Time Picker");
    }
    @BindView(R.id.btn_select_close)
    MaterialButton btnSelectClose;
    @OnClick(R.id.btn_select_close)
    void pickCloseTime() {
        timePickerDialog.setOnTimeSetListener(new android.app.TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                closeTime = convertTime(getTimeFromTimePicker(timePicker));
                btnSelectClose.setText("Pukul " + closeTime);
            }
        });
        timePickerDialog.show(getSupportFragmentManager(), "Close Time Picker");
    }
    @BindViews({R.id.chips_senin, R.id.chips_selasa, R.id.chips_rabu, R.id.chips_kamis, R.id.chips_jumat, R.id.chips_sabtu, R.id.chips_minggu})
    List<Chip> chips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_business_hours);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        businessHours = (ArrayList<BusinessHour>) getIntent().getExtras().getSerializable("business_hours");
        viewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(MerchantViewModel.class);
        initView();
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

    private void initView() {
        for (Chip chip: chips) {
            for (BusinessHour businessHour: businessHours) {
                if (chip.getText().equals(businessHour.convertDayToIndonesia())) {
                    chip.setChecked(true);
                }
            }
        }
    }

    private boolean setBusinessHours() {
        ArrayList<BusinessHour> businessHours = new ArrayList<>();
        for (Chip chip: chips) {
            if (chip.isChecked()) {
                String day = convertDayToEnglish(chip.getText().toString());
                BusinessHour businessHour = new BusinessHour(day, openTime, closeTime);
                businessHours.add(businessHour);
            }
        }
        if (businessHours.size() < 1) {
            return false;
        } else {
            this.businessHours = businessHours;
            return true;
        }
    }

    private void onFinishClicked() {
        boolean ready = true;
        if (openTime == null) {
            ready = false;
            showMessage("Anda belum memilih jam buka");
        }

        if (closeTime == null) {
            ready = false;
            showMessage("Anda belum memilih jam tutup");
        }

        if (ready) {
            if (setBusinessHours()) {
                update(new Merchant(businessHours));
            } else {
                showMessage("Anda belum memilih hari");
            }
        }
    }

    private void update(Merchant merchant) {
        viewModel.setBusinessHours(merchant).observe(SetBusinessHoursActivity.this, new Observer<Resource<Merchant>>() {
            @Override
            public void onChanged(Resource<Merchant> merchantResource) {
                switch (merchantResource.getStatus()) {
                    case SUCCESS:
                    case ERROR:
                    case EMPTY:
                        showMessage(merchantResource.getMessage());
                        break;
                }
            }
        });
    }

    private String getTimeFromTimePicker(TimePicker timePicker) {
        return timePicker.getHour() + ":" + timePicker.getMinute();
    }

    private String convertTime(String time) {
        Date convertedTime = null;
        try {
            convertedTime = new SimpleDateFormat("H:mm").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("HH:mm").format(convertedTime);
    }

    private String convertDayToEnglish(String dayInIndonesia) {
        String dayInEnglish = null;
        for (Day day: Day.values()) {
            if (day.getDayInIndonesia().equalsIgnoreCase(dayInIndonesia)) {
                dayInEnglish = day.getDayInEnglish();
            }
        }
        return dayInEnglish;
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

}
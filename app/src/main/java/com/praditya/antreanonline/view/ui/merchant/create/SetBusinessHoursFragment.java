package com.praditya.antreanonline.view.ui.merchant.create;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.BusinessHour;
import com.praditya.antreanonline.model.Day;
import com.praditya.antreanonline.model.Merchant;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.storage.SharedPreferencesManager;
import com.praditya.antreanonline.view.dialog.TimePickerDialog;
import com.praditya.antreanonline.viewmodel.MerchantViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SetBusinessHoursFragment extends Fragment {

    private final String TAG = getTag();
    private MerchantViewModel merchantViewModel;
    private Merchant merchant;
    private TimePickerDialog timePickerDialog = new TimePickerDialog();
    private String openTime = null;
    private String closeTime = null;
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
        timePickerDialog.show(getActivity().getSupportFragmentManager(), "Open Time Picker");
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
        timePickerDialog.show(getActivity().getSupportFragmentManager(), "Close Time Picker");
    }
    @BindViews({R.id.chips_senin, R.id.chips_selasa, R.id.chips_rabu, R.id.chips_kamis, R.id.chips_jumat, R.id.chips_sabtu, R.id.chips_minggu})
    List<Chip> chips;
//    @BindView(R.id.chips_senin)
//    Chip chipSenin;
//    @BindView(R.id.chips_selasa)
//    Chip chipSelasa;
//    @BindView(R.id.chips_rabu)
//    Chip chipRabu;
//    @BindView(R.id.chips_kamis)
//    Chip chipKamis;
//    @BindView(R.id.chips_jumat)
//    Chip chipJumat;
//    @BindView(R.id.chips_sabtu)
//    Chip chipSabtu;
//    @BindView(R.id.chips_minggu)
//    Chip chipMinggu;
    @OnClick(R.id.btn_finish_setbusinesshours)
    void finish() {
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
                createMerchant(merchant);
            } else {
                showMessage("Anda belum memilih hari");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_business_hours, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Atur Jam Operasional");
        merchant = (Merchant) getArguments().getSerializable("merchant");
        merchantViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(MerchantViewModel.class);
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
            merchant.setBusinessHours(businessHours);
            return true;
        }
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

    private void showMessage(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private void createMerchant(Merchant merchant) {
        merchantViewModel.create(merchant).observe(getViewLifecycleOwner(), new Observer<Resource<Merchant>>() {
            @Override
            public void onChanged(Resource<Merchant> merchantResource) {
                switch (merchantResource.getStatus()) {
                    case SUCCESS:
                        showMessage(merchantResource.getMessage());
                        SharedPreferencesManager.getSharedPreferencesManager(getContext()).setHasMerchant(true);
                        getActivity().finish();
                        break;
                    case ERROR:
                    case EMPTY:
                        showMessage(merchantResource.getMessage());
                        break;
                }
            }
        });
    }
}
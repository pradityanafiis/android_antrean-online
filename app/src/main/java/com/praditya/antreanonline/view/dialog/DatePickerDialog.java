package com.praditya.antreanonline.view.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

public class DatePickerDialog extends DialogFragment {

    private int maxDay;

    public DatePickerDialog(int maxDay) {
        this.maxDay = maxDay;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Locale.setDefault(Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(getActivity(), (android.app.DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
        calendar.add(Calendar.DAY_OF_YEAR, maxDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        return datePickerDialog;
    }
}

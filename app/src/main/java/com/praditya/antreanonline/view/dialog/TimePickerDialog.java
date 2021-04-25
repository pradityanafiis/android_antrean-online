package com.praditya.antreanonline.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerDialog extends DialogFragment {

    private android.app.TimePickerDialog.OnTimeSetListener onTimeSetListener;

    public void setOnTimeSetListener(android.app.TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        this.onTimeSetListener = onTimeSetListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, true);
        return timePickerDialog;
    }
}

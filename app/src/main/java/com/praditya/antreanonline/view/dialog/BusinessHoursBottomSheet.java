package com.praditya.antreanonline.view.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textview.MaterialTextView;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.BusinessHour;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class BusinessHoursBottomSheet extends BottomSheetDialogFragment {

    private ArrayList<BusinessHour> businessHours;
    @BindColor(R.color.colorRed)
    int red;
    @BindColor(R.color.colorGrey4)
    int grey;
    @BindViews({R.id.tv_senin, R.id.tv_selasa, R.id.tv_rabu, R.id.tv_kamis, R.id.tv_jumat, R.id.tv_sabtu, R.id.tv_minggu})
    List<MaterialTextView> textViews;

    public BusinessHoursBottomSheet(ArrayList<BusinessHour> businessHours) {
        this.businessHours = businessHours;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_business_hours, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        for (MaterialTextView textView: textViews) {
            textView.setTextColor(red);
        }

        for (MaterialTextView textView: textViews) {
            for (BusinessHour businessHour: businessHours) {
                if (textView.getText().toString().equalsIgnoreCase(businessHour.convertDayToIndonesia())) {
                    textView.setText(businessHour.getBusinessHourDisplay());
                    textView.setTextColor(grey);
                }
            }
        }

        for (MaterialTextView textView: textViews) {
            if (textView.getCurrentTextColor() == red) {
                textView.setText("Tutup");
            }
        }
    }
}

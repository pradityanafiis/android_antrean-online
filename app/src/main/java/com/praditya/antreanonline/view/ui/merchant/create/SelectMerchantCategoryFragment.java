package com.praditya.antreanonline.view.ui.merchant.create;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.snackbar.Snackbar;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.Merchant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SelectMerchantCategoryFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private Merchant merchant;
    @BindView(R.id.rg_category)
    RadioGroup radioGroup;
    @OnCheckedChanged({
            R.id.rb_bengkel,
            R.id.rb_cuci_kendaraan,
            R.id.rb_kecantikan,
            R.id.rb_kesehatan,
            R.id.rb_makanan,
            R.id.rb_pelayanan_publik,
            R.id.rb_pendidikan,
            R.id.rb_perbankan,
            R.id.rb_umum
    })
    void onCheckedChanged(RadioButton radioButton) {
        if (radioButton.isChecked()) {
            switch (radioButton.getId()) {
                case R.id.rb_bengkel:
                    merchant.setCategoryId(1);
                    break;
                case R.id.rb_cuci_kendaraan:
                    merchant.setCategoryId(2);
                    break;
                case R.id.rb_kecantikan:
                    merchant.setCategoryId(3);
                    break;
                case R.id.rb_kesehatan:
                    merchant.setCategoryId(4);
                    break;
                case R.id.rb_makanan:
                    merchant.setCategoryId(5);
                    break;
                case R.id.rb_pelayanan_publik:
                    merchant.setCategoryId(6);
                    break;
                case R.id.rb_pendidikan:
                    merchant.setCategoryId(7);
                    break;
                case R.id.rb_perbankan:
                    merchant.setCategoryId(8);
                    break;
                case R.id.rb_umum:
                    merchant.setCategoryId(9);
                    break;
            }
            setNextButtonEnabled(true);
        }
    }
    @BindView(R.id.btn_select_category)
    MaterialButton btnSelectCategory;
    @OnClick(R.id.btn_select_category)
    void finish() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("merchant", merchant);
        SetBusinessHoursFragment fragment = new SetBusinessHoursFragment();
        fragment.setArguments(bundle);
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("Select Merchant Category")
                .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_merchant_category, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Kategori Bisnis");
        merchant = (Merchant) getArguments().getSerializable("merchant");
        setNextButtonEnabled(false);
    }

    private void setNextButtonEnabled(boolean state) {
        btnSelectCategory.setEnabled(state);
    }

    private void showMessage(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
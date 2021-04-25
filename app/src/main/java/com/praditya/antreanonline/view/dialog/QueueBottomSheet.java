package com.praditya.antreanonline.view.dialog;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.Queue;
import com.praditya.antreanonline.model.QueueStatus;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.viewmodel.QueueViewModel;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QueueBottomSheet extends BottomSheetDialogFragment {

    private Queue queue;
    private Context context;
    private OnClickCallback onClickCallback;
    @BindColor(R.color.colorPrimary)
    int colorPrimary;
    @BindView(R.id.barcode)
    ImageView barcode;
    @BindView(R.id.tv_merchant_name)
    MaterialTextView tvMerchantName;
    @BindView(R.id.tv_service_name)
    MaterialTextView tvServiceName;
    @BindView(R.id.tv_queue_number)
    MaterialTextView tvQueueNumber;
    @BindView(R.id.tv_schedule)
    MaterialTextView tvSchedule;
    @BindView(R.id.btn_cancel_queue)
    MaterialButton btnCancelQueue;
    @OnClick(R.id.btn_cancel_queue)
    void cancelQueue() {
        onClickCallback.cancelQueue(queue);
        dismiss();
    }

    public QueueBottomSheet(Queue queue, Context context) {
        this.queue = queue;
        this.context = context;
    }

    public void setOnClickCallback(OnClickCallback onClickCallback) {
        this.onClickCallback = onClickCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_queue, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tvMerchantName.setText(queue.getService().getMerchant().getName());
        tvServiceName.setText(queue.getService().getName());
        tvQueueNumber.setText("Nomor Antrean : " + queue.getQueueNumber());
        tvSchedule.setText(queue.displaySchedule());

        QRGEncoder qrgEncoder = new QRGEncoder(String.valueOf(queue.getId()), null, QRGContents.Type.TEXT, 1000);
        Bitmap bitmap = qrgEncoder.getBitmap();
        Glide.with(this).load(bitmap).into(barcode);

        if (queue.getStatus().equals("waiting"))
            showCancelButton(true);
        else
            showCancelButton(false);
    }

    private void showCancelButton(boolean state) {
        if (state)
            btnCancelQueue.setVisibility(View.VISIBLE);
        else
            btnCancelQueue.setVisibility(View.GONE);
    }

    public interface OnClickCallback {
        void cancelQueue(Queue queue);
    }
}

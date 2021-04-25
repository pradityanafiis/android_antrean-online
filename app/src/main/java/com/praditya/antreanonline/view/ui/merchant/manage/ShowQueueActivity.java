package com.praditya.antreanonline.view.ui.merchant.manage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.Queue;
import com.praditya.antreanonline.model.QueueStatus;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.viewmodel.QueueViewModel;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowQueueActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private Queue queue;
    private QueueViewModel queueViewModel;
    @BindColor(R.color.colorGreen)
    int green;
    @BindColor(R.color.colorRed)
    int red;
    @BindColor(R.color.colorPrimary)
    int primary;
    @BindColor(R.color.colorOrange)
    int orange;
    @BindView(R.id.app_bar)
    Toolbar toolbar;
    @BindView(R.id.tv_queue_number)
    MaterialTextView tvQueueNumber;
    @BindView(R.id.tv_identity_number)
    MaterialTextView tvIdentityNumber;
    @BindView(R.id.tv_name)
    MaterialTextView tvName;
    @BindView(R.id.tv_service_name)
    MaterialTextView tvServiceName;
    @BindView(R.id.tv_schedule_date)
    MaterialTextView tvScheduleDate;
    @BindView(R.id.tv_schedule_time)
    MaterialTextView tvScheduleTime;
    @BindView(R.id.tv_queue_status)
    MaterialTextView tvQueueStatus;
    @BindView(R.id.btn_start)
    MaterialButton bntStart;
    @BindView(R.id.btn_finish)
    MaterialButton bntFinish;
    @OnClick(R.id.btn_start)
    void startServe() {
        updateStatus(QueueStatus.ACTIVE);
    }
    @OnClick(R.id.btn_finish)
    void finishServe() {
        updateStatus(QueueStatus.FINISH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_queue);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        queue = (Queue) getIntent().getExtras().getSerializable("queue");
        initView(queue);
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

    private void initView(Queue queue) {
        showButtonStart(false);
        showButtonFinish(false);
        tvQueueNumber.setText(String.valueOf(queue.getQueueNumber()));
        tvIdentityNumber.setText(queue.getUser().getIdentityNumber());
        tvName.setText(queue.getUser().getName());
        tvServiceName.setText(queue.getService().getName());
        tvScheduleDate.setText(queue.displayScheduleDate());
        tvScheduleTime.setText(queue.displayScheduleTime());
        switch (queue.statusToDisplay()) {
            case "Menunggu":
                tvQueueStatus.setTextColor(orange);
                showButtonStart(true);
                break;
            case "Aktif":
                tvQueueStatus.setTextColor(primary);
                showButtonFinish(true);
                break;
            case "Selesai":
                tvQueueStatus.setTextColor(green);
                break;
            case "Batal":
            case "Kedaluwarsa":
                tvQueueStatus.setTextColor(red);
                break;
        }
        tvQueueStatus.setText(queue.statusToDisplay());
    }

    private void initViewModel() {
        queueViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(QueueViewModel.class);
        queueViewModel.setQueue(queue);
        queueViewModel.updateStatus().observe(ShowQueueActivity.this, new Observer<Resource<Queue>>() {
            @Override
            public void onChanged(Resource<Queue> queueResource) {
                switch (queueResource.getStatus()) {
                    case SUCCESS:
                        initView(queueResource.getData());
                        break;
                    case EMPTY:
                    case ERROR:
                        showMessage(queueResource.getMessage());
                        break;
                }
            }
        });
    }

    private void updateStatus(QueueStatus queueStatus) {
        queueViewModel.setQueueStatus(queueStatus);
    }

    private void showButtonStart(boolean state) {
        if (state)
            bntStart.setVisibility(View.VISIBLE);
        else
            bntStart.setVisibility(View.GONE);
    }

    private void showButtonFinish(boolean state) {
        if (state)
            bntFinish.setVisibility(View.VISIBLE);
        else
            bntFinish.setVisibility(View.GONE);
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
package com.praditya.antreanonline.view.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.Queue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.ViewHolder> {

    private final String TAG = getClass().getSimpleName();
    private ArrayList<Queue> queues;
    private Context context;
    private OnClickCallback onClickCallback;

    public QueueAdapter(Context context) {
        this.context = context;
        queues = new ArrayList<>();
    }

    public void setQueues(ArrayList<Queue> queues) {
        this.queues = queues;
        notifyDataSetChanged();
    }

    public void setOnClickCallback(OnClickCallback onClickCallback) {
        this.onClickCallback = onClickCallback;
    }

    @NonNull
    @Override
    public QueueAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_queue, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QueueAdapter.ViewHolder holder, int position) {
        Queue queue = queues.get(position);
        holder.tvQueueNumber.setText(String.valueOf(queue.getQueueNumber()));
        holder.tvMerchantName.setText(queue.getService().getMerchant().getName());
        holder.tvServiceName.setText(queue.getService().getName());
        holder.tvSchedule.setText(queue.displaySchedule());
        switch (queue.statusToDisplay()) {
            case "Menunggu":
                holder.tvQueueStatus.setTextColor(holder.orange);
                break;
            case "Aktif":
                holder.tvQueueStatus.setTextColor(holder.primary);
                break;
            case "Selesai":
                holder.tvQueueStatus.setTextColor(holder.green);
                break;
            case "Batal":
            case "Kedaluwarsa":
                holder.tvQueueStatus.setTextColor(holder.red);
                break;
        }
        holder.tvQueueStatus.setText(queue.statusToDisplay());
        holder.cvQueue.setOnClickListener(view -> onClickCallback.showQueue(queue));
    }

    @Override
    public int getItemCount() {
        return queues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindColor(R.color.colorGreen)
        int green;
        @BindColor(R.color.colorRed)
        int red;
        @BindColor(R.color.colorPrimary)
        int primary;
        @BindColor(R.color.colorOrange)
        int orange;
        @BindView(R.id.cv_queue)
        MaterialCardView cvQueue;
        @BindView(R.id.tv_queue_number)
        MaterialTextView tvQueueNumber;
        @BindView(R.id.tv_merchant_name)
        MaterialTextView tvMerchantName;
        @BindView(R.id.tv_service_name)
        MaterialTextView tvServiceName;
        @BindView(R.id.tv_schedule)
        MaterialTextView tvSchedule;
        @BindView(R.id.tv_queue_status)
        MaterialTextView tvQueueStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClickCallback {
        void showQueue(Queue queue);
    }
}

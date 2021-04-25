package com.praditya.antreanonline.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.Service;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageServiceAdapter extends RecyclerView.Adapter<ManageServiceAdapter.ViewHolder> {

    private final String TAG = getClass().getSimpleName();
    private final Context context;
    private ArrayList<Service> services;
    private OnClickCallback onClickCallback;

    public ManageServiceAdapter(Context context) {
        this.context = context;
        this.services = new ArrayList<>();
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
        notifyDataSetChanged();
    }

    public void setOnClickCallback(OnClickCallback onClickCallback) {
        this.onClickCallback = onClickCallback;
    }

    @NonNull
    @Override
    public ManageServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manage_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageServiceAdapter.ViewHolder holder, int position) {
        Service service = services.get(position);
        holder.tvServiceName.setText(service.getName());
        holder.tvQuota.setText(service.getQuota() + " antrean");
        holder.tvInterval.setText(service.getInterval() + " menit");
        holder.tvMaxDay.setText(service.getMaxScheduledDay() + " hari");
        holder.cvManageService.setOnClickListener(view -> onClickCallback.showUpdateServiceDialog(service));
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv_service)
        MaterialCardView cvManageService;
        @BindView(R.id.tv_service_name)
        MaterialTextView tvServiceName;
        @BindView(R.id.tv_queue_quota)
        MaterialTextView tvQuota;
        @BindView(R.id.tv_interval)
        MaterialTextView tvInterval;
        @BindView(R.id.tv_max_day)
        MaterialTextView tvMaxDay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClickCallback {
        void showUpdateServiceDialog(Service service);
    }
}

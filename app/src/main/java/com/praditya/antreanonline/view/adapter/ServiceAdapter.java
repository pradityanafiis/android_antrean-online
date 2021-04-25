package com.praditya.antreanonline.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.Service;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    private final String TAG = getClass().getSimpleName();
    private ArrayList<Service> services;
    private Context context;
    private ServiceOnClickCallback serviceOnClickCallback;

    public ServiceAdapter(Context context) {
        this.context = context;
        this.services = new ArrayList<>();
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
        notifyDataSetChanged();
    }

    public void setServiceOnClickCallback(ServiceOnClickCallback serviceOnClickCallback) {
        this.serviceOnClickCallback = serviceOnClickCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Service service = services.get(position);
        holder.tvServiceName.setText(service.getName());
        holder.tvServiceDescription.setText(service.getDescription());
        holder.cvService.setOnClickListener(view -> serviceOnClickCallback.chooseService(service));
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.cv_service)
        MaterialCardView cvService;
        @BindView(R.id.service_name)
        MaterialTextView tvServiceName;
        @BindView(R.id.service_description)
        MaterialTextView tvServiceDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ServiceOnClickCallback {
        void chooseService(Service service);
    }
}

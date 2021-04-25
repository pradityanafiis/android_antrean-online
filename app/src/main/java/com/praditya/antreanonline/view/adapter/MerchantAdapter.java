package com.praditya.antreanonline.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.BusinessHour;
import com.praditya.antreanonline.model.Merchant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MerchantAdapter extends RecyclerView.Adapter<MerchantAdapter.ListViewHolder> {

    private final String TAG = getClass().getSimpleName();
    private ArrayList<Merchant> merchants;
    private Context context;
    private OnClickCallback onClickCallback;

    public MerchantAdapter(Context context) {
        this.context = context;
        merchants = new ArrayList<>();
    }

    public void setMerchants(ArrayList<Merchant> merchants) {
        this.merchants = merchants;
        notifyDataSetChanged();
    }

    public void setOnClickCallback(OnClickCallback onClickCallback) {
        this.onClickCallback = onClickCallback;
    }

    @NonNull
    @Override
    public MerchantAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_merchant, parent, false);
        ButterKnife.bind(this, view);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MerchantAdapter.ListViewHolder holder, int position) {
        Merchant merchant = merchants.get(position);
        String day = LocalDate.now().getDayOfWeek().name();

        Glide.with(context).load(decodeImage(merchant.getPhoto())).centerCrop().into(holder.imageView);
        holder.tvMerchantName.setText(merchant.getName());
        if (merchant.isOpen(day)) {
            BusinessHour businessHour = merchant.getTodayBusinessHour(day);
            holder.tvBusinessHour.setText(businessHour.getBusinessHourDisplay());
            if (businessHour.isAlreadyClose())
                holder.tvBusinessHour.setTextColor(holder.red);
            else
                holder.tvBusinessHour.setTextColor(holder.green);
        } else {
            holder.tvBusinessHour.setText("Tutup");
            holder.tvBusinessHour.setTextColor(holder.red);
        }
        holder.cvMerchant.setOnClickListener(view -> onClickCallback.showMerchant(merchant));
    }

    @Override
    public int getItemCount() {
        return merchants.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cv_merchant)
        CardView cvMerchant;
        @BindView(R.id.iv_merchant_photo)
        ImageView imageView;
        @BindView(R.id.merchant_name)
        TextView tvMerchantName;
        @BindView(R.id.tv_business_hour)
        TextView tvBusinessHour;
        @BindColor(R.color.colorGreen)
        int green;
        @BindColor(R.color.colorRed)
        int red;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private Bitmap decodeImage(String image) {
        byte[] imageBytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    public interface OnClickCallback {
        void showMerchant(Merchant merchant);
    }
}

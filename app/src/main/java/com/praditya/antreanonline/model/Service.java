package com.praditya.antreanonline.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Service implements Serializable {
    private int id;

    @SerializedName("merchant_id")
    private int merchantId;

    private String name;

    private String description;

    private int quota;

    private int interval;

    @SerializedName("max_scheduled_day")
    private int maxScheduledDay;

    private Merchant merchant;

    public Service(String name, String description, int quota, int interval, int maxScheduledDay) {
        this.name = name;
        this.description = description;
        this.quota = quota;
        this.interval = interval;
        this.maxScheduledDay = maxScheduledDay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getMaxScheduledDay() {
        return maxScheduledDay;
    }

    public void setMaxScheduledDay(int maxScheduledDay) {
        this.maxScheduledDay = maxScheduledDay;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
}

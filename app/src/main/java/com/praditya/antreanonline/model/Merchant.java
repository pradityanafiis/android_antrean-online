package com.praditya.antreanonline.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Merchant implements Serializable {
    private int id;

    @SerializedName("category_id")
    private int categoryId;

    @SerializedName("user_id")
    private int userId;

    private String name;

    private String address;

    private double latitude;

    private double longitude;

    private String phone;

    private String photo;

    private ArrayList<Service> services;

    @SerializedName("businesshours")
    private ArrayList<BusinessHour> businessHours;

    public Merchant(String name, String address, double latitude, double longitude, String phone) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
    }

    public Merchant(ArrayList<BusinessHour> businessHours) {
        this.businessHours = businessHours;
    }

    public boolean isOpen(String day) {
        for (BusinessHour businessHour: businessHours) {
            if (businessHour.getDayOfWeek().equalsIgnoreCase(day))
                return true;
        }
        return false;
    }

    public BusinessHour getTodayBusinessHour(String day) {
        BusinessHour todayBusinessHour = null;
        for (BusinessHour businessHour: businessHours) {
            if (businessHour.getDayOfWeek().equalsIgnoreCase(day))
                todayBusinessHour = businessHour;
        }
        return todayBusinessHour;
    }

    public String getLocationUri() {
        return "https://www.google.com/maps/search/?api=1&query=" + this.latitude + "," + this.longitude;
    }

    public String getPhoneUri() {
        return "tel:+62" + this.phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }

    public ArrayList<BusinessHour> getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(ArrayList<BusinessHour> businessHours) {
        this.businessHours = businessHours;
    }
}

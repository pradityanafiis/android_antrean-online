package com.praditya.antreanonline.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    @SerializedName("identity_number")
    private String identityNumber;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String token;
    private String photo;
    @SerializedName("has_merchant")
    private boolean hasMerchant;

    public User(String identityNumber, String name, String phone, String email, String password) {
        this.identityNumber = identityNumber;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public User(String name, String phone, String email, String token, String photo, boolean hasMerchant) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.token = token;
        this.photo = photo;
        this.hasMerchant = hasMerchant;
    }

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isHasMerchant() {
        return hasMerchant;
    }

    public void setHasMerchant(boolean hasMerchant) {
        this.hasMerchant = hasMerchant;
    }
}

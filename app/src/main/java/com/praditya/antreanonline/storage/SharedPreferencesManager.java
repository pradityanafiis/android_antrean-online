package com.praditya.antreanonline.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.praditya.antreanonline.model.User;

public class SharedPreferencesManager {
    private static final String SHARED_PREFERENCES_NAME = "AntreanOnline_SharedPreferences";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static SharedPreferencesManager sharedPreferencesManager;
    private Context context;

    public SharedPreferencesManager(Context context) {
        this.context = context;
    }

    public static synchronized SharedPreferencesManager getSharedPreferencesManager(Context context) {
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = new SharedPreferencesManager(context);
            sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        return sharedPreferencesManager;
    }

    public void saveUser(User user) {
        editor.putString("name", user.getName());
        editor.putString("email", user.getEmail());
        editor.putString("phone", user.getPhone());
        editor.putString("token", "Bearer " + user.getToken());
        editor.putString("photo", user.getPhoto());
        editor.putBoolean("hasmerchant", user.isHasMerchant());
        editor.apply();
    }

    public void updateUser(User user) {
        editor.putString("name", user.getName());
        editor.putString("email", user.getEmail());
        editor.putString("phone", user.getPhone());
        editor.putString("photo", user.getPhoto());
        editor.apply();
    }


    public User getUser() {
        String name = sharedPreferences.getString("name", null);
        String email = sharedPreferences.getString("email", null);
        String phone = sharedPreferences.getString("phone", null);
        String token = sharedPreferences.getString("token", null);
        String photo = sharedPreferences.getString("photo", null);
        boolean hasMerchant = sharedPreferences.getBoolean("hasmerchant", false);
        return new User(name, phone, email, token, photo, hasMerchant);
    }

    public String getName() {
        return sharedPreferences.getString("name", "Full Name");
    }

    public String getToken() {
        return sharedPreferences.getString("token", "");
    }

    public void setHasMerchant(boolean isHasMerchant) {
        editor.putBoolean("hasmerchant", isHasMerchant);
        editor.apply();
    }

    public void deleteUser() {
        editor.clear().apply();
    }

    public boolean isLogin() {
        if (getToken().equals("")) {
            return false;
        }
        return true;
    }
}

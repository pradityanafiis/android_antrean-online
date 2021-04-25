package com.praditya.antreanonline.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BusinessHour implements Serializable {
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat timeFormatWithoutSecond = new SimpleDateFormat("HH:mm");

    private int id;

    @SerializedName("merchant_id")
    private int merchantId;

    @SerializedName("day_of_week")
    private String dayOfWeek;

    @SerializedName("open_time")
    private String openTime;

    @SerializedName("close_time")
    private String closeTime;

    public BusinessHour(String dayOfWeek, String openTime, String closeTime) {
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public int getId() {
        return id;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public String convertDayToIndonesia() {
        String dayInIndonesia = null;
        for (Day day: Day.values()) {
            if (day.getDayInEnglish().equalsIgnoreCase(this.dayOfWeek)) {
                dayInIndonesia = day.getDayInIndonesia();
            }
        }
        return dayInIndonesia;
    }

    public String getBusinessHourDisplay() {
        Date openTime = null;
        Date closeTime = null;
        try {
            openTime = timeFormat.parse(this.getOpenTime());
            closeTime = timeFormat.parse(this.getCloseTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "Pukul " + timeFormatWithoutSecond.format(openTime) + " - " + timeFormatWithoutSecond.format(closeTime);
    }

    private Date parseCloseTime() {
        Date closeTime = null;
        try {
            closeTime = timeFormatWithoutSecond.parse(this.closeTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return closeTime;
    }

    private Date getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        String currentTimeString = timeFormatWithoutSecond.format(calendar.getTime());
        Date currentTime = null;
        try {
            currentTime = timeFormatWithoutSecond.parse(currentTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currentTime;
    }

    public boolean isAlreadyClose() {
        return getCurrentTime().compareTo(parseCloseTime()) > 0;
    }
}

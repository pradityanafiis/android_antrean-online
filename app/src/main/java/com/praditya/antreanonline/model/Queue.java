package com.praditya.antreanonline.model;

import android.text.format.DateFormat;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Queue implements Serializable {
    private int id;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("service_id")
    private int serviceId;

    private String schedule;

    @SerializedName("queue_number")
    private int queueNumber;

    @SerializedName("estimated_time_serve")
    private String estimatedTimeServe;

    @SerializedName("start_time_serve")
    private String startTimeServe;

    @SerializedName("finish_time_serve")
    private String finishTimeServe;

    private String status;

    private Service service;

    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getQueueNumber() {
        return queueNumber;
    }

    public void setQueueNumber(int queueNumber) {
        this.queueNumber = queueNumber;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getEstimatedTimeServe() {
        return estimatedTimeServe;
    }

    public void setEstimatedTimeServe(String estimatedTimeServe) {
        this.estimatedTimeServe = estimatedTimeServe;
    }

    public String getStartTimeServe() {
        return startTimeServe;
    }

    public void setStartTimeServe(String startTimeServe) {
        this.startTimeServe = startTimeServe;
    }

    public String getFinishTimeServe() {
        return finishTimeServe;
    }

    public void setFinishTimeServe(String finishTimeServe) {
        this.finishTimeServe = finishTimeServe;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getScheduleDay() {
        Locale.setDefault(Locale.ENGLISH);
        Date schedule = null;
        try {
            schedule = new SimpleDateFormat("yyyy-MM-dd").parse(this.getSchedule());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (String) DateFormat.format("EEEE", schedule);
    }

    public String getScheduleDayIndonesia() {
        String dayInIndonesia = null;
        for (Day day: Day.values()) {
            if (day.getDayInEnglish().equals(this.getScheduleDay())) {
                dayInIndonesia = day.getDayInIndonesia();
            }
        }
        return dayInIndonesia;
    }

    public String displaySchedule() {
        Locale.setDefault(Locale.ENGLISH);
        Date date = null;
        Date time = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(this.getSchedule());
            time = new SimpleDateFormat("HH:mm:ss").parse(this.getEstimatedTimeServe());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateFormatted = (String) DateFormat.format("dd-MM-yyyy", date);
        String timeFormatted = (String) new SimpleDateFormat("HH:mm").format(time);
        return getScheduleDayIndonesia() + ", " + dateFormatted + " (Pukul " + timeFormatted + ")";
    }

    public String displayScheduleDate() {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(this.getSchedule());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateFormatted = (String) DateFormat.format("dd-MM-yyyy", date);
        return getScheduleDayIndonesia() + ", " + dateFormatted;
    }

    public String displayScheduleTime() {
        Date time = null;
        try {
            time = new SimpleDateFormat("HH:mm:ss").parse(this.getEstimatedTimeServe());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String timeFormatted = (String) new SimpleDateFormat("HH:mm").format(time);
        return "Pukul " + timeFormatted;
    }

    public String statusToDisplay() {
        String status = null;
        for (QueueStatus queueStatus: QueueStatus.values()) {
            if (queueStatus.getStatusInDatabase().equals(this.status)) {
                status = queueStatus.getStatusToDisplay();
            }
        }
        return status;
    }
}

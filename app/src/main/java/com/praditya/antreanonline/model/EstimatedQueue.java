package com.praditya.antreanonline.model;

import com.google.gson.annotations.SerializedName;

public class EstimatedQueue {
    @SerializedName("queue_number")
    private int queueNumber;

    @SerializedName("estimated_time_serve")
    private String estimatedTimeServe;

    public int getQueueNumber() {
        return queueNumber;
    }

    public String getEstimatedTimeServe() {
        return estimatedTimeServe;
    }
}

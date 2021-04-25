package com.praditya.antreanonline.model;

public enum QueueStatus {
    WAITING("waiting", "Menunggu"),
    ACTIVE("active", "Aktif"),
    FINISH("finish", "Selesai"),
    CANCELED("canceled", "Batal"),
    EXPIRED("expired", "Kedaluwarsa");

    private String statusInDatabase;
    private String statusToDisplay;

    QueueStatus(String statusInDatabase, String statusToDisplay) {
        this.statusInDatabase = statusInDatabase;
        this.statusToDisplay = statusToDisplay;
    }

    public String getStatusInDatabase() {
        return statusInDatabase;
    }

    public String getStatusToDisplay() {
        return statusToDisplay;
    }
}

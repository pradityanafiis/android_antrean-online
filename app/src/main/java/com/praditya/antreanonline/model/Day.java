package com.praditya.antreanonline.model;

public enum Day {
    MONDAY("Monday", "Senin"),
    TUESDAY("Tuesday", "Selasa"),
    WEDNESDAY("Wednesday", "Rabu"),
    THURSDAY("Thursday", "Kamis"),
    FRIDAY("Friday", "Jumat"),
    SATUDRAY("Saturday", "Sabtu"),
    SUNDAY("Sunday", "Minggu");

    private String dayInEnglish;
    private String dayInIndonesia;

    Day(String dayInEnglish, String dayInIndonesia) {
        this.dayInEnglish = dayInEnglish;
        this.dayInIndonesia = dayInIndonesia;
    }

    public String getDayInEnglish() {
        return dayInEnglish;
    }

    public String getDayInIndonesia() {
        return dayInIndonesia;
    }
}

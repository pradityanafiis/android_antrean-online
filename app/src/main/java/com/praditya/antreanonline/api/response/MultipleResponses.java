package com.praditya.antreanonline.api.response;

import java.util.ArrayList;

public class MultipleResponses<T> {
    private boolean error;
    private String message;
    private ArrayList<T> data;

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<T> getData() {
        return data;
    }
}

package com.example.bookstore.model;

import java.util.List;

public class ChapterResponse {

    private boolean status;
    private String message;
    private List<Chapter>  data;

    public ChapterResponse(boolean status, String message, List<Chapter> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Chapter> getData() {
        return data;
    }

    public void setData(List<Chapter> data) {
        this.data = data;
    }
}

package com.example.bookstore.model;

public class LogoutResponse {

    private boolean status;
    private String message;

    public LogoutResponse(boolean status, String message) {
        this.status = status;
        this.message = message;

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

}

package com.example.bookstore.model;

public class RegisterResponse {

    private boolean status;
    private String message;

    private UserInfo data;

    public RegisterResponse(boolean status, String message, UserInfo data) {
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

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}

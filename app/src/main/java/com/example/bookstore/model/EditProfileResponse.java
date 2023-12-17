package com.example.bookstore.model;

public class EditProfileResponse {

    private boolean status;
    private String message, token;

    private UserInfo data;

    public EditProfileResponse(boolean status, String message, String token, UserInfo data) {
        this.status = status;
        this.message = message;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}

package com.example.bookstore.model;

import java.util.List;

public class BankInfoResponse {

    private boolean status;
    private String message;
    private List<BankInfo> data;

    public BankInfoResponse(boolean status, String message, List<BankInfo> data) {
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

    public List<BankInfo> getData() {
        return data;
    }

    public void setData(List<BankInfo> data) {
        this.data = data;
    }
}

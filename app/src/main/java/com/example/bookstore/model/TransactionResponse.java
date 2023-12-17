package com.example.bookstore.model;

import java.util.List;

public class TransactionResponse {

    private boolean status;
    private String message;
    private List<Transaction>  data;

    public TransactionResponse(boolean status, String message, List<Transaction> data) {
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

    public List<Transaction> getData() {
        return data;
    }

    public void setData(List<Transaction> data) {
        this.data = data;
    }
}

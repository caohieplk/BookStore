package com.example.bookstore.dto;

import java.util.List;

public class SearchBookResponse {

    private boolean status;
    private String message;
    private List<Book> data;

    public SearchBookResponse(boolean status, String message, List<Book> data) {
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

    public List<Book> getData() {
        return data;
    }

    public void setData(List<Book> data) {
        this.data = data;
    }
}

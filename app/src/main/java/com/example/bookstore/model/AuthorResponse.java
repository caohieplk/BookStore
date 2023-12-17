package com.example.bookstore.model;

public class AuthorResponse {

    private boolean status;
    private String message;
    private Author data;

    public AuthorResponse(boolean status, String message, Author data) {
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

    public Author getData() {
        return data;
    }

    public void setData(Author data) {
        this.data = data;
    }
}

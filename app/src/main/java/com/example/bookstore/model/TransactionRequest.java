package com.example.bookstore.model;

public class TransactionRequest {
    private int id_book;

    public TransactionRequest() {
    }

    public TransactionRequest(int id) {
        this.id_book = id;
    }

    public int getId() {
        return id_book;
    }

    public void setId(int id) {
        this.id_book = id;
    }
}

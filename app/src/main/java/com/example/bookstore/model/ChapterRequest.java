package com.example.bookstore.model;

public class ChapterRequest {
    private int id_book;

    public ChapterRequest() {
    }

    public ChapterRequest(int id) {
        this.id_book = id;
    }

    public int getId() {
        return id_book;
    }

    public void setId(int id) {
        this.id_book = id;
    }
}

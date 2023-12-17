package com.example.bookstore.model;

public class CategoryRequest {
    private int id_category;

    public CategoryRequest() {
    }

    public CategoryRequest(int id_category) {
        this.id_category = id_category;
    }

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }
}

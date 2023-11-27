package com.example.bookstore.dto;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private int id;

    private String name, image, description, name_en;

    private List<Book> books = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public Category(int id, String name, String image, String description, String name_en, List<Book> books) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.name_en = name_en;
        this.books = books;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public List<Book> getBooks() {
        return books == null ? new ArrayList<>() : books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
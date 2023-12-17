package com.example.bookstore.model;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private int id;

    private String name, image;

    private List<Book> books = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public Category(int id, String name, String image, List<Book> books) {
        this.id = id;
        this.name = name;
        this.image = image;
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

    public List<Book> getBooks() {
        return books == null ? new ArrayList<>() : books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
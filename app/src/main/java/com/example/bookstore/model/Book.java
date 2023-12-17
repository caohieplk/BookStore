package com.example.bookstore.model;

public class Book {

    private int id, id_category, id_author, read_count, favorite;

    private String code, name, image, description, created_at, top, price, banner;

    public Book(int id) {
        this.id = id;
    }

    public Book(int id, int id_category, int id_author, int read_count, int favorite, String code, String name, String image, String description, String created_at, String top, String price, String banner) {
        this.id = id;
        this.id_category = id_category;
        this.id_author = id_author;
        this.read_count = read_count;
        this.favorite = favorite;
        this.code = code;
        this.name = name;
        this.image = image;
        this.description = description;
        this.created_at = created_at;
        this.top = top;
        this.price = price;
        this.banner = banner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }

    public int getId_author() {
        return id_author;
    }

    public void setId_author(int id_author) {
        this.id_author = id_author;
    }

    public int getRead_count() {
        return read_count;
    }

    public void setRead_count(int read_count) {
        this.read_count = read_count;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getPrice() {
        return price == null ? "" : price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public int getFavorite() {
        return favorite;
    }

    public boolean isFavorite() {
        return favorite == 1;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
}
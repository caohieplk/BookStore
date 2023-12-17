package com.example.bookstore.model;

public class Transaction {
    private int id, id_book, id_user, status;
    private String title, created_at, price, payment;


    public Transaction() {
    }

    public Transaction(int id) {
        this.id = id;
    }

    public Transaction(int id, int id_book, int id_user, int status, String title, String created_at, String price, String payment) {
        this.id = id;
        this.id_book = id_book;
        this.id_user = id_user;
        this.status = status;
        this.title = title;
        this.created_at = created_at;
        this.price = price;
        this.payment = payment;
    }

    public Transaction(int id_book, int id_user, int status, String title, String created_at, String price, String payment) {
        this.id_book = id_book;
        this.id_user = id_user;
        this.status = status;
        this.title = title;
        this.created_at = created_at;
        this.price = price;
        this.payment = payment;
    }

    public Transaction(int id_book, int id_user) {
        this.id_book = id_book;
        this.id_user = id_user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_book() {
        return id_book;
    }

    public void setId_book(int id_book) {
        this.id_book = id_book;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusString() {
        if (status == 0) return "Thanh toán thất bại";
        else if (status == 1) return "Đã thanh toán";
        return "Đã thanh toán";
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}

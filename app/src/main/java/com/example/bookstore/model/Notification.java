package com.example.bookstore.model;

public class Notification {
    private int id;
    private String name;
    private String price1;
    private String price2;
    private String day;

    public Notification() {
    }

    public Notification(int id, String name, String price1, String price2, String day) {
        this.id = id;
        name = name;
        this.price1 = price1;
        this.price2 = price2;
        day = day;
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
        name = name;
    }

    public String getPrice1() {
        return price1;
    }

    public void setPrice1(String price1) {
        this.price1 = price1;
    }

    public String getPrice2() {
        return price2;
    }

    public void setPrice2(String price2) {
        this.price2 = price2;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        day = day;
    }
}

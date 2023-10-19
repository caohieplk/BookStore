package com.example.bookstore.dto;

public class NotificationResponeDTO {
    private int id;
    private String name ,price1 ,price2,day;

    public NotificationResponeDTO() {
    }

    public NotificationResponeDTO(int id, String name, String price1, String price2, String day) {
        this.id = id;
        this.name = name;
        this.price1 = price1;
        this.price2 = price2;
        this.day = day;
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
        this.day = day;
    }
}

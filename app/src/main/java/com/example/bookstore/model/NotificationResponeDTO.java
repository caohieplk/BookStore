package com.example.bookstore.model;

public class NotificationResponeDTO {
    private int id;
    private String name ,price1 ,price2,image,day;

    public NotificationResponeDTO() {
    }

    public NotificationResponeDTO(int id, String name, String price1, String price2, String image, String day) {
        this.id = id;
        this.name = name;
        this.price1 = price1;
        this.price2 = price2;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}

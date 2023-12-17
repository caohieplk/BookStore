package com.example.bookstore.model;

public class BankInfo {

    private int id;

    private String user_name, bank_name, bank_detail, bank_number;

    public BankInfo(int id, String user_name, String bank_name, String bank_detail, String bank_number) {
        this.id = id;
        this.user_name = user_name;
        this.bank_name = bank_name;
        this.bank_detail = bank_detail;
        this.bank_number = bank_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_detail() {
        return bank_detail;
    }

    public void setBank_detail(String bank_detail) {
        this.bank_detail = bank_detail;
    }

    public String getBank_number() {
        return bank_number;
    }

    public void setBank_number(String bank_number) {
        this.bank_number = bank_number;
    }
}
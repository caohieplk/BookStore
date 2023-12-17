package com.example.bookstore.app_util.event_bus;



public class MessageEvent {

    public static final String CHANGE_STATE_FAVORITE = "CHANGE_STATE_FAVORITE";
    public static final String PURCHASED_BOOK = "PAYMENTED_BOOK";



    private String msg;

    public MessageEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



}

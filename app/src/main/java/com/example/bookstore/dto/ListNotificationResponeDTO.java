package com.example.bookstore.dto;

import java.util.List;

public class ListNotificationResponeDTO {
    private boolean status;
    private String message;
    private List<NotificationResponeDTO> data;

    public ListNotificationResponeDTO(boolean status, String message, List<NotificationResponeDTO> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ListNotificationResponeDTO() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<NotificationResponeDTO> getData() {
        return data;
    }

    public void setData(List<NotificationResponeDTO> data) {
        this.data = data;
    }
}

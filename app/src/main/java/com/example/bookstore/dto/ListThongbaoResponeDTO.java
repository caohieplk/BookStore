package com.example.bookstore.dto;

import java.util.List;

public class ListThongbaoResponeDTO {

    private boolean status;
    private String message;
    private List<ThongbaoResponseDTO> data;

    public ListThongbaoResponeDTO() {
    }

    public ListThongbaoResponeDTO(boolean status, String message, List<ThongbaoResponseDTO> data) {
        this.status = status;
        this.message = message;
        this.data = data;
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

    public List<ThongbaoResponseDTO> getData() {
        return data;
    }

    public void setData(List<ThongbaoResponseDTO> data) {
        this.data = data;
    }
}

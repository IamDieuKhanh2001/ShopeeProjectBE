package com.example.fsoft_shopee_nhom02.dto;

public class UpdateOrderDTO {
    private long id;
    private String status;

    public UpdateOrderDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String statas) {
        this.status = statas;
    }

    public UpdateOrderDTO(long id, String statas) {
        this.id = id;
        this.status = statas;
    }
}

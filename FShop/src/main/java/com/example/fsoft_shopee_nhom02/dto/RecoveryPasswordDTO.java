package com.example.fsoft_shopee_nhom02.dto;

public class RecoveryPasswordDTO {
    String password;

    public RecoveryPasswordDTO(String password) {
        this.password = password;
    }

    public RecoveryPasswordDTO() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

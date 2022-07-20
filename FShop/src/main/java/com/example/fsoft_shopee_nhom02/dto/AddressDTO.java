package com.example.fsoft_shopee_nhom02.dto;

public class AddressDTO {

    private String address;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AddressDTO(String address, String username) {
        this.address = address;
        this.username = username;
    }

    public AddressDTO() {
    }

    public AddressDTO(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

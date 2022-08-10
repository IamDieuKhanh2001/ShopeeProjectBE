package com.example.fsoft_shopee_nhom02.dto;

public class AddressDTO {

    private String address;
    private String name;
    private String phone;
    private Boolean addressDefault;

    public Boolean getAddressDefault() {
        return addressDefault;
    }

    public void setAddressDefault(Boolean addressDefault) {
        this.addressDefault = addressDefault;
    }

    public AddressDTO(String address, String name, String phone) {
        this.address = address;
        this.name = name;
        this.phone = phone;
    }

    public AddressDTO() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

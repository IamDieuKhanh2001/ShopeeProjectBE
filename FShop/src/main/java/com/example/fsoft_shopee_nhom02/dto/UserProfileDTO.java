package com.example.fsoft_shopee_nhom02.dto;

import com.example.fsoft_shopee_nhom02.model.AddressEntity;
import com.example.fsoft_shopee_nhom02.model.BaseClassEntity;
import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import com.example.fsoft_shopee_nhom02.model.RoleEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

public class UserProfileDTO extends BaseClassEntity{
    private String username;
    private String name;
    private String email;
    private String phone;
    private String gender;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Timestamp dob;
    private String avatar;
    private List<AddressEntity> addressEntityList;
    private List<OrderEntity> orderEntities;

    public UserProfileDTO() {
    }

    public UserProfileDTO(String username, String name, String email, String phone, String gender, Timestamp dob, String avatar, List<AddressEntity> addressEntityList, List<OrderEntity> orderEntities) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
        this.avatar = avatar;
        this.addressEntityList = addressEntityList;
        this.orderEntities = orderEntities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Timestamp getDob() {
        return dob;
    }

    public void setDob(Timestamp dob) {
        this.dob = dob;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<AddressEntity> getAddressEntityList() {
        return addressEntityList;
    }

    public void setAddressEntityList(List<AddressEntity> addressEntityList) {
        this.addressEntityList = addressEntityList;
    }

    public List<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    public void setOrderEntities(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }
}

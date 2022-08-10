package com.example.fsoft_shopee_nhom02.dto;

import com.example.fsoft_shopee_nhom02.model.BaseClassEntity;
import com.example.fsoft_shopee_nhom02.model.RoleEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Set;

public class UserDTO extends BaseClassEntity {

    private long id;
    private String username;
    private String password;
    private String phone;
    private String name;
    private String gender;
    private String email;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Timestamp dob;
    private int age;
    private String avatar;
    private Set<RoleEntity> roleEntitySet;

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", dob=" + dob +
                ", avatar='" + avatar + '\'' +
                '}';
    }

    public UserDTO(String name, String gender, Timestamp dob) {
        this.name = name;
        this.gender = gender;
        this.dob = dob;
    }

    public UserDTO(String username, String password, String phone, String email) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }

    public UserDTO(String username, String password, String phone, String name, String gender, String email, Timestamp dob, String avatar) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.dob = dob;
        this.avatar = avatar;
    }

    public UserDTO() {
    }

    public UserDTO(String username, String password, String phone, String name, String gender, String email, String avatar) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.avatar = avatar;
    }

    public UserDTO(long id, String username, String password, String phone, String name, String gender, String email, Timestamp dob, String avatar, Set<RoleEntity> roleEntitySet) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.dob = dob;
        this.avatar = avatar;
        this.roleEntitySet = roleEntitySet;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserDTO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Set<RoleEntity> getRoleEntitySet() {
        return roleEntitySet;
    }

    public void setRoleEntitySet(Set<RoleEntity> roleEntitySet) {
        this.roleEntitySet = roleEntitySet;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge(){
        Calendar dobCal = Calendar.getInstance();
        dobCal.setTimeInMillis(dob.getTime());
        return Calendar.getInstance().get(Calendar.YEAR) - dobCal.get(Calendar.YEAR) ;
    }
}

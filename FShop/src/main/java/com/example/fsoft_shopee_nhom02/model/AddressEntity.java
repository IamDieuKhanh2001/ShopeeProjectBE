package com.example.fsoft_shopee_nhom02.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private String name;
    private String phoneNumber;
    private String ward;
    private String city;
    private String district;
    @Column(columnDefinition = "boolean default false")
    private Boolean addressDefault = false;

    //  Tạo quan hệ với bảng User
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "userId", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    //category_id là trường khóa phụ ta tạo ra ở bảng SubCategory để referenced đến trường id của bảng Category
    private UserEntity userEntity; //Liên hệ với MapBy ở Category

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    // Constructor, Getter, Setter
    public AddressEntity() {
    }

    public AddressEntity(String address, String name, String phoneNumber, UserEntity userEntity) {
        this.address = address;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.userEntity = userEntity;
    }

    public AddressEntity(Long id, String address, String name, String phoneNumber, Boolean addressDefault, UserEntity userEntity) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.addressDefault = addressDefault;
        this.userEntity = userEntity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public AddressEntity(String address, UserEntity userEntity) {
        this.address = address;
        this.userEntity = userEntity;
    }

    public Boolean getAddressDefault() {
        return addressDefault;
    }

    public void setAddressDefault(Boolean addressDefault) {
        this.addressDefault = addressDefault;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AddressEntity(String address, String name, String phoneNumber, String ward, String city, String district, UserEntity userEntity) {
        this.address = address;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.ward = ward;
        this.city = city;
        this.district = district;
        this.userEntity = userEntity;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return "AddressEntity{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", userEntity=" + userEntity +
                '}';
    }
}

package com.example.fsoft_shopee_nhom02.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String status;
    private String userName;
    private String address;
    private String note;
    private String payment;
    private Long shippingFee;
    private Long totalPrice;

    // Tạo quan hệ với ProductEntity
    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL)
    private List<ProductEntity> productEntities =new ArrayList<>();

    // Constructor, Getter, Setter
    public OrderEntity() {
    }

    public OrderEntity(Long id, String status, String userName, String address, String note, String payment, Long shippingFee, Long totalPrice) {
        this.id = id;
        this.status = status;
        this.userName = userName;
        this.address = address;
        this.note = note;
        this.payment = payment;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public Long getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Long shippingFee) {
        this.shippingFee = shippingFee;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }
}

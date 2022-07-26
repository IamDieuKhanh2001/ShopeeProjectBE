package com.example.fsoft_shopee_nhom02.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity extends BaseClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private String userName;
    private String address;
    private String phone;
    private String note;
    private String payment;
    private Long shippingFee;
    private Long totalPrice;

    //Tạo quan hệ với UserEntity (n:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "userId", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    //category_id là trường khóa phụ ta tạo ra ở bảng SubCategory để referenced đến trường id của bảng Category
    private UserEntity userEntities; //Liên hệ với MapBy ở Category

    // Tạo quan hệ với OrderDetailsEntity
    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL)
    private List<OrderDetailsEntity> orderDetailsEntities = new ArrayList<>();

    // Constructor, Getter, Setter
    public OrderEntity() {
    }

    public OrderEntity(Long id, String status, String userName, String address, String note, String payment, Long shippingFee, Long totalPrice, String phone) {
        this.id = id;
        this.status = status;
        this.userName = userName;
        this.address = address;
        this.note = note;
        this.payment = payment;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public void setUserId(Long id) {
        this.userEntities = new UserEntity();
        this.userEntities.setId(id);
    }

    public Long getUserId() {
        return this.userEntities.getId();
    }
}

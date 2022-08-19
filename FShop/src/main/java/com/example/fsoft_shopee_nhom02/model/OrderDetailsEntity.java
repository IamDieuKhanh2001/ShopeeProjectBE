package com.example.fsoft_shopee_nhom02.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "orderdetails")
//@JsonIgnoreProperties({"orderEntity"})
public class OrderDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long unitPrice;
    private Long quantity;
    private String type;
    //  Relationship with table Order
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "orderId", nullable = false, referencedColumnName = "id")
    private OrderEntity orderEntity;

    //  Relationship with table Product
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "productId", nullable = false, referencedColumnName = "id")
    private ProductEntity productEntity;

    // Constructor, Getter, Setter
    public OrderDetailsEntity() {
    }

    public OrderDetailsEntity(Long id, Long unitPrice, Long quantity, String type) {
        this.id = id;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.type = type;
    }

    public OrderDetailsEntity(Long unitPrice, Long quantity, String type) {
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productEntity.getId();
    }

    public Long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public void setProductEntityID(long id) {
        this.productEntity = new ProductEntity();
        this.productEntity.setId(id);
    }

    public Long getProductEntityID(long id) {
        return this.productEntity.getId();
    }

    public void setOrderEntityID(long id) {
        this.orderEntity = new OrderEntity();
        this.orderEntity.setId(id);
    }

    public Long getOrderEntityID(long id) {
        return this.orderEntity.getId();
    }

    //    public OrderEntity getOrderEntity() {
//        return orderEntity;
//    }
//
    public void setOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }
}

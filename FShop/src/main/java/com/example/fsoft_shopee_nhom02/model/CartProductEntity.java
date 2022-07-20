package com.example.fsoft_shopee_nhom02.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "carts_products")
public class CartProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long quantity;
    private String type;

    //  Tạo quan hệ với bảng Cart
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "cartId", nullable = false, referencedColumnName = "id")
    //cartId là trường khóa phụ ta tạo ra ở bảng SubCategory để referenced đến trường id của bảng CartEntity
    private CartEntity cartEntity; //Liên hệ với MapBy ở CartEntity

    //  Tạo quan hệ với bảng Product
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "productId", nullable = false, referencedColumnName = "id")
    //productId là trường khóa phụ ta tạo ra ở bảng SubCategory để referenced đến trường id của bảng ProductEntity
    private ProductEntity productEntity; //Liên hệ với MapBy ở ProductEntity

    public CartProductEntity() {
    }

    public CartProductEntity(Long quantity, String type) {
        this.type = type;
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public CartProductEntity(Long id, Long quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CartEntity getCartEntity() {
        return cartEntity;
    }

    public void setCartEntity(CartEntity cartEntity) {
        this.cartEntity = cartEntity;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }
    // constructor, getter, setter
}

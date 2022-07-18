package com.example.fsoft_shopee_nhom02.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tạo quan hệ với UserEntity
    @OneToOne(mappedBy = "cartEntity")
    @JsonManagedReference
    private UserEntity userEntity;

    // Tạo quan hệ với ProductEntity
    @OneToMany(mappedBy = "cartEntity", cascade = CascadeType.ALL)
    private List<ProductEntity> productEntities =new ArrayList<>();

    // Constructor, Getter, Setter
    public CartEntity() {
    }

    public CartEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

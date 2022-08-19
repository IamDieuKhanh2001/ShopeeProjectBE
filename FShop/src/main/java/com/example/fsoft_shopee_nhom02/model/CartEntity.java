package com.example.fsoft_shopee_nhom02.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "carts")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationship with table UserEntity
    @OneToOne(mappedBy = "cartEntity")
    private UserEntity userEntity;


    // Relationship with table CartProductEntity
    @OneToMany(mappedBy = "cartEntity", cascade = CascadeType.ALL)
    private List<CartProductEntity> cartProductEntities =new ArrayList<>();


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

package com.example.fsoft_shopee_nhom02.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String image;
    @Column(name="status", columnDefinition="Varchar(255) default 'Active'")
    private String status;
    // Relationship with table SubCategoryEntity
    @OneToMany(mappedBy = "categoryEntity", cascade = CascadeType.ALL)
    private List<SubCategoryEntity> subCategoryEntities =new ArrayList<>();

    // Relationship with table ShopEntity
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "shopId", nullable = false, referencedColumnName = "id")
    private ShopEntity shopEntity;

    // Constructor, Getter, Setter
    public CategoryEntity() {
    }

    public CategoryEntity(Long id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ShopEntity getShopEntity() {
        return shopEntity;
    }

    public void setShopEntity(ShopEntity shopEntity) {
        this.shopEntity = shopEntity;
    }
}

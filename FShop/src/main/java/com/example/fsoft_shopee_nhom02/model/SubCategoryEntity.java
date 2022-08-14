package com.example.fsoft_shopee_nhom02.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subcategories")
public class SubCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String image;
    @Column(name="status", columnDefinition="Varchar(255) default 'Active'")
    private String status;
    // Tạo quan hệ với CategoryEntity
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "category_id", nullable = false, referencedColumnName = "id")
    private CategoryEntity categoryEntity;

    // Tạo quan hệ với ProductEntity
    @OneToMany(mappedBy = "subCategoryEntity", cascade = CascadeType.ALL)
    private List<ProductEntity> productEntities =new ArrayList<>();

    //Constructor, Getter, Setter
    public SubCategoryEntity() {
    }

    public SubCategoryEntity(Long id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public SubCategoryEntity(Long id, String name, String image, String status) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.status = status;
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

    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }
}

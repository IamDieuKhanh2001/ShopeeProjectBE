package com.example.fsoft_shopee_nhom02.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String detail;
    private String description;
    private String imageProduct;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private Long sale;
    private Long fromPrice;
    private Double avgRating;
    @Column(name="totalview", columnDefinition="Bigint default '0'")
    private Long totalView;
    @Column(name="sold", columnDefinition="Bigint default '0'")
    private Long sold;
    @Column(name="status", columnDefinition="Varchar(255) default 'Active'")
    private String status;
    // Relationship with table CartProductEntity
    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL)
    private List<CartProductEntity> cartProductEntities =new ArrayList<>();


    // Relationship with table OrderDetailsEntity
    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL)
    private List<OrderDetailsEntity> orderDetailsEntities =new ArrayList<>();

    // Relationship with table SubCategoryEntity
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "sub_category_id", nullable = false, referencedColumnName = "id")
    private SubCategoryEntity subCategoryEntity;

    // Relationship with table TypeEntity
    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL)
    private List<TypeEntity> typeEntities =new ArrayList<>();
    // Relationship with table TypeEntity
    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL)
    private List<CommentEntity> commentEntities =new ArrayList<>();

    // Constructor, Getter, Setter
    public ProductEntity() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProductEntity(Long id, String name, String detail, String description, String imageProduct, String image1, String image2, String image3, String image4, Long sale) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.description = description;
        this.imageProduct = imageProduct;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.sale = sale;
    }

    public ProductEntity(Long id, String name, String detail, String description, String imageProduct, String image1, String image2, String image3, String image4, Long sale, Long totalView, Long sold) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.description = description;
        this.imageProduct = imageProduct;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.sale = sale;
        this.totalView = totalView;
        this.sold = sold;
    }

    public Long getTotalView() {
        return totalView;
    }

    public void setTotalView(Long totalView) {
        this.totalView = totalView;
    }

    public Long getSold() {
        return sold;
    }

    public void setSold(Long sold) {
        this.sold = sold;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageProduct() {
        return imageProduct;
    }

    public void setImageProduct(String imageProduct) {
        this.imageProduct = imageProduct;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public Long getSale() {
        return sale;
    }

    public void setSale(Long sale) {
        this.sale = sale;
    }

    public Long getFromPrice() {
        return fromPrice;
    }

    public void setFromPrice(Long fromPrice) {
        this.fromPrice = fromPrice;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public SubCategoryEntity getSubCategoryEntity() {
        return subCategoryEntity;
    }

    public void setSubCategoryEntity(SubCategoryEntity subCategoryEntity) {
        this.subCategoryEntity = subCategoryEntity;
    }

    public long getSubCategoryId() {
        return subCategoryEntity.getId();
    }

    public long getCatId() {
        return subCategoryEntity.getCategoryEntity().getId();
    }

    public List<TypeEntity> getTypeEntities() {
        return typeEntities;
    }

    public void setTypeEntities(List<TypeEntity> typeEntities) {
        this.typeEntities = typeEntities;
    }
}

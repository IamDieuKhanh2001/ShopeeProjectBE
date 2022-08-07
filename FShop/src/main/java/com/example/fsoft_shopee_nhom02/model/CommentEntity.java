package com.example.fsoft_shopee_nhom02.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseClassEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private String image;
    private Long userid;
    private long rating;
    @Column(name="status", columnDefinition="Varchar(255) default 'Active'")
    private String status;

    // Tạo quan hệ với ProductEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "productId", nullable = false, referencedColumnName = "id")
    private ProductEntity productEntity;

    // Constructor, Getter, Setter
    public CommentEntity() {
    }

    public CommentEntity(Long id, String comment, String image, Long userid) {
        this.id = id;
        this.comment = comment;
        this.image = image;
        this.userid = userid;
    }

    public CommentEntity(Long id, String comment, String image, Long userid, long rating, String status) {
        this.id = id;
        this.comment = comment;
        this.image = image;
        this.userid = userid;
        this.rating = rating;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }
}

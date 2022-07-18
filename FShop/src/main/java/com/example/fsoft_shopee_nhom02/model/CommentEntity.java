package com.example.fsoft_shopee_nhom02.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String comment;
    private String image;
    private String userid;

    // Tạo quan hệ với ProductEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "productId", nullable = false, referencedColumnName = "id")
    private ProductEntity productEntity;

    // Constructor, Getter, Setter
    public CommentEntity() {
    }

    public CommentEntity(Long id, String comment, String image, String userid) {
        this.id = id;
        this.comment = comment;
        this.image = image;
        this.userid = userid;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}

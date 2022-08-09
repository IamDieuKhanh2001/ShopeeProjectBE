package com.example.fsoft_shopee_nhom02.dto;

public class SubCategoryDTO {
    private Long id;
    private String name;
    private String image;
    private String status = "Active";
    private Long categoryId;

    public SubCategoryDTO(Long id, String name, String image, String status, Long categoryId) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.status = status;
        this.categoryId = categoryId;
    }

    public SubCategoryDTO() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}

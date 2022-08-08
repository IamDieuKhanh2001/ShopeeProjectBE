package com.example.fsoft_shopee_nhom02.dto;

public class CategoryDTO {
    private Long id;
    private String name;
    private String image;
    private String status = "Active";
    private Long shopId;

    public CategoryDTO(Long id, String name, String image, String status, Long shopId) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.status = status;
        this.shopId = shopId;
    }

    public CategoryDTO() {
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

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
}

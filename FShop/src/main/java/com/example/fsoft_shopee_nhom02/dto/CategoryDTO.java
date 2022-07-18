package com.example.fsoft_shopee_nhom02.dto;

public class CategoryDTO {
    private Long id;
    private String name;
    private String image;
    private Long shopId;

    public CategoryDTO(Long id, String name, String image, Long shopId) {
        this.id = id;
        this.name = name;
        this.image = image;
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

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
}

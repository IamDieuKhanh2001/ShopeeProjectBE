package com.example.fsoft_shopee_nhom02.dto;

public class ShopDTO {
    private Long id;
    private String name;
    private String status;
    private Long totalProduct;
    private String description;
    private String avatar;
    private String background;

    public ShopDTO() {

    }

    public ShopDTO(Long id, String name, String status, Long totalProduct, String description, String avatar, String background) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.totalProduct = totalProduct;
        this.description = description;
        this.avatar = avatar;
        this.background = background;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTotalProduct() {
        return totalProduct;
    }

    public void setTotalProduct(Long totalProduct) {
        this.totalProduct = totalProduct;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}

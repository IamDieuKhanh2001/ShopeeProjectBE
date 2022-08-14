package com.example.fsoft_shopee_nhom02.dto;

import com.example.fsoft_shopee_nhom02.model.TypeEntity;

import java.util.List;

public class ProductDTO {
    private long id;
    private String name;
    private String description;
    private String detail;
    private double avgRating;
    private String imageProduct;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private long price;
    private long sale;
    private long sold;
    private long totalView;
    private long subCategoryId;
    private long catId;

    public ProductDTO() {
    }

    public ProductDTO(long id, String name, String description, String detail, double avgRating, String imageProduct, String image1, String image2, String image3, String image4, long price, long sale, long sold, long totalView, long subCategoryId, long catId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.detail = detail;
        this.avgRating = avgRating;
        this.imageProduct = imageProduct;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.price = price;
        this.sale = sale;
        this.sold = sold;
        this.totalView = totalView;
        this.subCategoryId = subCategoryId;
        this.catId = catId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Long getSale() {
        return sale;
    }

    public void setSale(Long sale) {
        this.sale = sale;
    }

    public long getSold() {
        return sold;
    }

    public void setSold(long sold) {
        this.sold = sold;
    }

    public long getTotalView() {
        return totalView;
    }

    public void setTotalView(long totalView) {
        this.totalView = totalView;
    }

    public long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public long getCatId() {
        return catId;
    }

    public void setCatId(long catId) {
        this.catId = catId;
    }
}

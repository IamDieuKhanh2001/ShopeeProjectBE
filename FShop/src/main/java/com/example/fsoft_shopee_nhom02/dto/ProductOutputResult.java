package com.example.fsoft_shopee_nhom02.dto;

import java.util.List;

public class ProductOutputResult {
    private long productsNumber;
    private List<ProductDTO> productsList;

    public ProductOutputResult() {
    }

    public ProductOutputResult(long productsNumber, List<ProductDTO> productsList) {
        this.productsNumber = productsNumber;
        this.productsList = productsList;
    }

    public long getProductsNumber() {
        return productsNumber;
    }

    public void setProductsNumber(long productsNumber) {
        this.productsNumber = productsNumber;
    }

    public List<ProductDTO> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<ProductDTO> productsList) {
        this.productsList = productsList;
    }
}

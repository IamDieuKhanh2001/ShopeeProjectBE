package com.example.fsoft_shopee_nhom02.dto;

import java.util.List;

public class ProductSearchResult {
    private String keyWord;
    private long productsNumber;
    private List<ProductDTO> productsList;

    public ProductSearchResult() {
    }

    public ProductSearchResult(String keyWord, int productsNumber, List<ProductDTO> productsList) {
        this.keyWord = keyWord;
        this.productsNumber = productsNumber;
        this.productsList = productsList;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
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

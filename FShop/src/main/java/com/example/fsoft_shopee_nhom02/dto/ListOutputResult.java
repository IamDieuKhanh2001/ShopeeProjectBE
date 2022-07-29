package com.example.fsoft_shopee_nhom02.dto;

import java.util.List;

public class ListOutputResult {
    private long itemsNumber;
    private List<?> List;

    public ListOutputResult() {
    }

    public ListOutputResult(long itemsNumber, java.util.List<?> list) {
        this.itemsNumber = itemsNumber;
        List = list;
    }

    public long getItemsNumber() {
        return itemsNumber;
    }

    public void setItemsNumber(long itemsNumber) {
        this.itemsNumber = itemsNumber;
    }

    public java.util.List<?> getList() {
        return List;
    }

    public void setList(java.util.List<?> list) {
        List = list;
    }
}

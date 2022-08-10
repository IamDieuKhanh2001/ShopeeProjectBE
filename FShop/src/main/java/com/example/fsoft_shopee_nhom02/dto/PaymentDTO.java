package com.example.fsoft_shopee_nhom02.dto;

public class PaymentDTO {
    private long id;
    private long orderId;
    private int amount;
    private String description;
    private String bankCode;

    public PaymentDTO(Long id, Long orderId,int amount, String description, String bankCode) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.description = description;
        this.bankCode = bankCode;
    }

    public PaymentDTO() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}

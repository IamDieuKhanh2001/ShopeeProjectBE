package com.example.fsoft_shopee_nhom02.dto;

public class PaymentResultDTO {
    private String status;
    private String Message;
    private String Url;

    public PaymentResultDTO() {
    }

    public PaymentResultDTO(String status, String message, String url) {
        this.status = status;
        this.Message = message;
        this.Url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    @Override
    public String toString() {
        return "Payment Result {" +
                "status='" + status + '\'' +
                ", Message='" + Message + '\'' +
                ", Url='" + Url + '\'' +
                '}';
    }
}

package com.example.fsoft_shopee_nhom02.dto;

public class OtpSendMailResponseDTO {
    private String status;
    private String otp;

    public OtpSendMailResponseDTO(String status, String otp) {
        this.status = status;
        this.otp = otp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}

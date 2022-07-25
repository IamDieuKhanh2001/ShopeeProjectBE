package com.example.fsoft_shopee_nhom02.dto;

import org.springframework.http.HttpStatus;

public class SuccessResponseDTO {
    private HttpStatus status;
    private String successMessage;

    public SuccessResponseDTO(HttpStatus status, String successMessage) {
        this.status = status;
        this.successMessage = successMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}

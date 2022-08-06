package com.example.fsoft_shopee_nhom02.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("twilio")
public class TwilioConfig {

    private String accountSid;
    private String authToken;
    private String fromNumber;

    public TwilioConfig() {
    }

    public String getAccountSid() {
        return accountSid;
    }
    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }
    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    public String getFromNumber() {
        return fromNumber;
    }
    public void setFromNumber(String fromNumber) {
        this.fromNumber = fromNumber;
    }

}

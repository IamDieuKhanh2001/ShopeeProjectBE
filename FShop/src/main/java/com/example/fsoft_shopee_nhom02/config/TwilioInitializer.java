package com.example.fsoft_shopee_nhom02.config;

import com.twilio.Twilio;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioInitializer {

    private final TwilioConfig twilioConfig;

    public TwilioInitializer(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
        System.out.println("twilio intialized with account: "+ twilioConfig.getAccountSid());
    }
}

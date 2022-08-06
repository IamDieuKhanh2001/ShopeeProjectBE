package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.config.TwilioConfig;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SmsSenderService {

    @Autowired
    private TwilioConfig twilioConfig;

    public String sendsms(String phoneNumber, String text)
    {
        Message message= Message.creator(
                new PhoneNumber(phoneNumber),
                new PhoneNumber("+12029801897"),
//                new PhoneNumber(twilioConfig.getFromNumber()),
                text
        ).create();
        return message.getStatus().toString();
    }
}

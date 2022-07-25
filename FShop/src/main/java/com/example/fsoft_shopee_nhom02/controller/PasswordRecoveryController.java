package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.dto.RecoveryPasswordDTO;
import com.example.fsoft_shopee_nhom02.model.AddressEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.service.EmailSenderService;
import com.example.fsoft_shopee_nhom02.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/recoveryPassword")
public class PasswordRecoveryController {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    UserService userService;

    @GetMapping(path = "/getOtp")
    public RecoveryPasswordDTO recoveryByEmail(@RequestParam Map<String, String> requestParams) {
        if(requestParams.get("email") == null) {
            throw new IllegalStateException("change password by address for user fail! need ?email= param");
        }
        String emailRecovery = (requestParams.get("email"));
        UserEntity user = userService.getUsersByEmail(emailRecovery).get(0); //Kiem tra user voi email khoi phuc
        String otpCode = generateOtpCode();
        try {
            sendRecoveryEmail(emailRecovery, user.getUsername(), otpCode);
        } catch (MessagingException e) {
            throw new IllegalStateException("gmail gửi thất bại");
        }

        return new RecoveryPasswordDTO("Valid", otpCode);
    }

    public void sendRecoveryEmail(String addressGmail, String username, String otpCode) throws MessagingException {
        emailSenderService.sendSimpleEmail(addressGmail,
                "Yêu cầu thay đổi mật khẩu cho " + username,
                "Bạn đã yêu cầu thay đổi mật khẩu cho tài khoản " + username + " \n " +
                        "Mã OTP của bạn là: " + otpCode + " \n " +
                        "Cám ơn bạn đã sử dụng dịch vụ" );
    }

    public String generateOtpCode() {
        // It will generate 6 digit random Number.
        // from 000000 to 999999
        Random otpCodeGenerator = new Random();
        int otpNumber = otpCodeGenerator.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", otpNumber);
    }
}

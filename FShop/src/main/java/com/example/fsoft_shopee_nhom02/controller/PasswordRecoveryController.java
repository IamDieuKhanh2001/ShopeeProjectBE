package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.config.EmailTemplate;
import com.example.fsoft_shopee_nhom02.config.GlobalVariable;
import com.example.fsoft_shopee_nhom02.dto.OtpSendMailResponseDTO;
import com.example.fsoft_shopee_nhom02.dto.RecoveryPasswordDTO;
import com.example.fsoft_shopee_nhom02.dto.SuccessResponseDTO;
import com.example.fsoft_shopee_nhom02.exception.BadRequest;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.service.EmailSenderService;
import com.example.fsoft_shopee_nhom02.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Map;

@RestController
@RequestMapping("/recoveryPassword")
public class PasswordRecoveryController {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    UserService userService;

    @GetMapping(path = "/getOtp")
    public OtpSendMailResponseDTO sendOtpRecoveryCodeToUserEmail(@RequestParam Map<String, String> requestParams) {
        if(requestParams.get("email") == null) {
            throw new BadRequest("change password by address for user fail! need ?email= param");
        }
        String emailRecovery = (requestParams.get("email"));
        UserEntity user = userService.getUsersByEmail(emailRecovery).get(0); //Kiem tra user voi email khoi phuc
        String otpCode = GlobalVariable.GetOTP();
        try {
            sendRecoveryEmail(emailRecovery, user.getUsername(), otpCode);
        } catch (MessagingException e) {
            throw new BadRequest("gmail send fail");
        }

        return new OtpSendMailResponseDTO("Valid", otpCode);
    }

    public void sendRecoveryEmail(String addressGmail, String username, String otpCode) throws MessagingException {
        emailSenderService.sendAsHTML(
                addressGmail,
                "Bạn đã Yêu cầu khôi phục mật khẩu cho " + username,
                EmailTemplate.TemplateRecoveryPassword(username, otpCode)
        );
    }

    @PutMapping(path = "/recovery/{email}")
    public SuccessResponseDTO recoveryPassword(@PathVariable String email,@RequestBody RecoveryPasswordDTO userChangePassword) {
        userService.changeUserPasswordByEmail(userChangePassword.getPassword(), email);
        return new SuccessResponseDTO(HttpStatus.OK, "Change password success");
    }
}

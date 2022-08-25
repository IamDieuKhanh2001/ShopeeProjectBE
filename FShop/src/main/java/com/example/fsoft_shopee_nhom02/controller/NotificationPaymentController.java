package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.service.impl.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/checkout")
public class NotificationPaymentController {
    @Autowired
    PaymentServiceImpl paymentService;

    @GetMapping("notification")
    public ResponseEntity<?> returnResultPayment(HttpServletRequest request){
        if (request.getParameter("vnp_ResponseCode").equals("24"))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment failed!");
        }
        return paymentService.acceptOrder(Long.valueOf(request.getParameter("vnp_TxnRef")));
    }
}

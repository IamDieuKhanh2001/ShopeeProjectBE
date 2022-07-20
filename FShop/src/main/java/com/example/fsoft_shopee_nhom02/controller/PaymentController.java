package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.repository.TypeRepository;
import com.example.fsoft_shopee_nhom02.service.OrderDetailService;
import com.example.fsoft_shopee_nhom02.service.impl.OrderDetailServiceImpl;
import com.example.fsoft_shopee_nhom02.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    OrderServiceImpl orderService;
    @Autowired
    OrderDetailServiceImpl orderDetailService;
    @Autowired
    TypeRepository typeRepository;

    @PutMapping("/{id}")
    public ResponseEntity<?> paymentOrder(@PathVariable long id){

        return null;
    }

}

package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.config.PaymentConfig;
import com.example.fsoft_shopee_nhom02.dto.PaymentDTO;
import com.example.fsoft_shopee_nhom02.dto.PaymentResultDTO;
import com.example.fsoft_shopee_nhom02.model.OrderDetailsEntity;
import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import com.example.fsoft_shopee_nhom02.model.ProductEntity;
import com.example.fsoft_shopee_nhom02.model.TypeEntity;
import com.example.fsoft_shopee_nhom02.repository.ProductRepository;
import com.example.fsoft_shopee_nhom02.repository.TypeRepository;
import com.example.fsoft_shopee_nhom02.service.impl.OrderDetailServiceImpl;
import com.example.fsoft_shopee_nhom02.service.impl.OrderServiceImpl;
import com.example.fsoft_shopee_nhom02.service.impl.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    OrderServiceImpl orderService;
    @Autowired
    OrderDetailServiceImpl orderDetailService;
    @Autowired
    TypeRepository typeRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    PaymentServiceImpl paymentService;

    @PostMapping("payment-vnpay")
    public ResponseEntity<?> paymentVnpay(@RequestBody PaymentDTO paymentDTO) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        OrderEntity order = orderService.findOrderById(paymentDTO.getOrderId());
        if (order == null)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't find an order!");
        }
        Map<String, String> vnp_Params = paymentService.returnParamVnPay(paymentDTO);
        String paymentUrl = paymentService.returnPaymentUrl(vnp_Params);
        PaymentResultDTO paymentResultDTO = new PaymentResultDTO("00", "Success", paymentUrl);
        return ResponseEntity.status(HttpStatus.OK).body(paymentResultDTO.toString());
    }

    @PutMapping("accept/{id}")
    public ResponseEntity<?> acceptOrder(@PathVariable long id){
        return paymentService.acceptOrder(id);
    }

    @PutMapping("cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable long id){
        return paymentService.cancelOrder(id);
    }
}

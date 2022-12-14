package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.auth.OrderCreator;
import com.example.fsoft_shopee_nhom02.config.GlobalVariable;
import com.example.fsoft_shopee_nhom02.config.PaymentConfig;
import com.example.fsoft_shopee_nhom02.dto.AddressDTO;
import com.example.fsoft_shopee_nhom02.dto.OrderIdDTO;
import com.example.fsoft_shopee_nhom02.dto.PaymentDTO;
import com.example.fsoft_shopee_nhom02.dto.PaymentResultDTO;
import com.example.fsoft_shopee_nhom02.model.*;
import com.example.fsoft_shopee_nhom02.repository.OrderRepository;
import com.example.fsoft_shopee_nhom02.repository.ProductRepository;
import com.example.fsoft_shopee_nhom02.repository.TypeRepository;
import com.example.fsoft_shopee_nhom02.repository.UserRepository;
import com.example.fsoft_shopee_nhom02.service.AddressService;
import com.example.fsoft_shopee_nhom02.service.CartProductService;
import com.example.fsoft_shopee_nhom02.service.UserService;
import com.example.fsoft_shopee_nhom02.service.impl.OrderDetailServiceImpl;
import com.example.fsoft_shopee_nhom02.service.impl.OrderServiceImpl;
import com.example.fsoft_shopee_nhom02.service.impl.PaymentServiceImpl;
import com.example.fsoft_shopee_nhom02.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.example.fsoft_shopee_nhom02.auth.ApplicationUserService.GetUsernameLoggedIn;
import static com.example.fsoft_shopee_nhom02.config.GlobalVariable.getCurrentDateTime;

@RestController
@RequestMapping("/admin/payment")
public class PaymentController {
    @Autowired
    PaymentServiceImpl paymentService;

    @PutMapping("accept")
    public ResponseEntity<?> acceptOrder(@RequestBody OrderIdDTO orderId){
        return paymentService.acceptOrder(orderId.getId());
    }

    @PutMapping("cancel")
    public ResponseEntity<?> cancelOrder(@RequestBody OrderIdDTO orderId){
        return paymentService.cancelOrder(orderId.getId());
    }
}

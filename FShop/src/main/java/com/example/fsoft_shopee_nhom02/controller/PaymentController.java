package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.auth.OrderCreator;
import com.example.fsoft_shopee_nhom02.config.GlobalVariable;
import com.example.fsoft_shopee_nhom02.config.PaymentConfig;
import com.example.fsoft_shopee_nhom02.dto.AddressDTO;
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
    OrderServiceImpl orderService;
    @Autowired
    OrderDetailServiceImpl orderDetailService;
    @Autowired
    TypeRepository typeRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    PaymentServiceImpl paymentService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AddressService addressService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    CartProductService cartProductService;

    @PostMapping("payment-vnpay")
    public ResponseEntity<?> paymentVnpay(@RequestBody PaymentDTO paymentDTO) throws UnsupportedEncodingException, NoSuchAlgorithmException, ParseException {
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

    @PostMapping("payment-vnpay2")
    public ResponseEntity<?> createPaymentVnpay(@RequestBody List<Object> req) throws UnsupportedEncodingException, NoSuchAlgorithmException, ParseException {
        OrderEntity orderEntity = OrderCreator.CreateOrder(userService, addressService, orderService, cartProductService,req, GlobalVariable.ORDER_STATUS.VNPAY_CONFIRM.toString());

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setOrderId(orderEntity.getId());
        paymentDTO.setBankCode(PaymentConfig.BANKCODE);
        paymentDTO.setAmount((int) (Math.toIntExact(orderEntity.getTotalPrice())+orderEntity.getShippingFee()));
        paymentDTO.setDescription(GlobalVariable.ORDER_STATUS.VNPAY_CONFIRM.toString());

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

    @GetMapping("resultPayment")
    public ResponseEntity<?> returnResultPayment(HttpServletRequest request){
        if (request.getParameter("vnp_ResponseCode").equals("24"))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment failed!");
        }
        return paymentService.acceptOrder(Long.valueOf(request.getParameter("vnp_TxnRef")));
    }

}

package com.example.fsoft_shopee_nhom02.service.impl;

import com.example.fsoft_shopee_nhom02.config.GlobalVariable;
import com.example.fsoft_shopee_nhom02.config.PaymentConfig;
import com.example.fsoft_shopee_nhom02.dto.PaymentDTO;
import com.example.fsoft_shopee_nhom02.model.OrderDetailsEntity;
import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import com.example.fsoft_shopee_nhom02.model.ProductEntity;
import com.example.fsoft_shopee_nhom02.model.TypeEntity;
import com.example.fsoft_shopee_nhom02.repository.ProductRepository;
import com.example.fsoft_shopee_nhom02.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PaymentServiceImpl {
    @Autowired
    OrderServiceImpl orderService;
    @Autowired
    OrderDetailServiceImpl orderDetailService;
    @Autowired
    TypeRepository typeRepository;
    @Autowired
    ProductRepository productRepository;

    public ResponseEntity<?> acceptOrder(Long id)
    {
        OrderEntity order = orderService.findOrderById(id);
        if (order == null)
        {
            return new ResponseEntity<>("Can't find an order!", HttpStatus.NOT_FOUND);
        }
        List<OrderDetailsEntity> orderDetailsEntityList = orderDetailService.findAllByOrderEntityId(id);
        orderDetailsEntityList.stream().forEach(orderDetail -> {
            TypeEntity type = typeRepository.findProductByType(orderDetail.getProductId(), orderDetail.getType());
            ProductEntity product = productRepository.findById(orderDetail.getProductId()).get();
            // Update quantity type
            if (type.getQuantity() <= orderDetail.getQuantity())
            {
                type.setQuantity(type.getQuantity() - orderDetail.getQuantity());
            }
            typeRepository.save(type);
            // Update product sold
            product.setSold(product.getSold() + orderDetail.getQuantity());
            productRepository.save(product);
        });
        // Set status order
        order.setStatus(GlobalVariable.ORDER_STATUS.DONE.toString());
        orderService.updateOrder(order);
        return ResponseEntity.ok("Payment success!");
    }


    public ResponseEntity<?> cancelOrder(Long id)
    {
        OrderEntity order = orderService.findOrderById(id);
        if (order == null)
        {
            return new ResponseEntity<>("Can't find an order!", HttpStatus.NOT_FOUND);
        }
        order.setStatus(GlobalVariable.ORDER_STATUS.CANCELED.toString());
        orderService.updateOrder(order);
        return ResponseEntity.ok("Cancel order success!");
    }

    public Map<String, String> returnParamVnPay(PaymentDTO paymentDTO)
    {
        int amount = paymentDTO.getAmount()*100;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", PaymentConfig.VERSIONVNPAY);
        vnp_Params.put("vnp_Command", PaymentConfig.COMMAND);
        vnp_Params.put("vnp_TmnCode", PaymentConfig.TMNCODE);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", PaymentConfig.CURRCODE);
        vnp_Params.put("vnp_BankCode", paymentDTO.getBankCode());
        vnp_Params.put("vnp_TxnRef", String.valueOf(paymentDTO.getOrderId()));
        vnp_Params.put("vnp_OrderInfo", paymentDTO.getDescription());
        vnp_Params.put("vnp_OrderType", PaymentConfig.ORDERTYPE);
        vnp_Params.put("vnp_Locale", PaymentConfig.LOCALEDEFAULT);
        vnp_Params.put("vnp_ReturnUrl", PaymentConfig.RETURNURL + paymentDTO.getOrderId()); //add id order
        vnp_Params.put("vnp_IpAddr", PaymentConfig.IPDEFAULT);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = format.format(date);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        return vnp_Params;
    }

    public String returnPaymentUrl(Map<String, String> vnp_Params) throws UnsupportedEncodingException {
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String vnp_SecureHash = PaymentConfig.hmacSHA512(PaymentConfig.CHECKSUM, hashData.toString());
        String queryUrl = query.toString() + "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = PaymentConfig.VNPAYURL + "?" + queryUrl;
        return paymentUrl;
    }
}

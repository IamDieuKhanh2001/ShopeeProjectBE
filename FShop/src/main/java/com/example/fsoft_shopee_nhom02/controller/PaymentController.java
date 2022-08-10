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

    @PostMapping("payment-vnpay")
    public ResponseEntity<?> paymentVnpay(@RequestBody PaymentDTO paymentDTO) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        OrderEntity order = orderService.findOrderById(paymentDTO.getOrderId());
        int amount = paymentDTO.getAmount()*100;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", PaymentConfig.VERSIONVNPAY);
        vnp_Params.put("vnp_Command", PaymentConfig.COMMAND);
        vnp_Params.put("vnp_TmnCode", PaymentConfig.TMNCODE);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", PaymentConfig.CURRCODE);
        vnp_Params.put("vnp_BankCode", paymentDTO.getBankCode());
        vnp_Params.put("vnp_TxnRef", String.valueOf(order.getId()));
        vnp_Params.put("vnp_OrderInfo", paymentDTO.getDescription());
        vnp_Params.put("vnp_OrderType", PaymentConfig.ORDERTYPE);
        vnp_Params.put("vnp_Locale", PaymentConfig.LOCALEDEFAULT);
        vnp_Params.put("vnp_ReturnUrl", PaymentConfig.RETURNURL);
        vnp_Params.put("vnp_IpAddr", PaymentConfig.IPDEFAULT);

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = format.format(date);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//        vnp_Params.put("vnp_Bill_Mobile", order.getPhone());
//        String fullName = order.getUserName();
//        if (fullName != null && !fullName.isEmpty()) {
//            int idx = fullName.indexOf(' ');
//            String firstName = fullName.substring(0, idx);
//            String lastName = fullName.substring(fullName.lastIndexOf(' ') + 1);
//            vnp_Params.put("vnp_Bill_FirstName", firstName);
//            vnp_Params.put("vnp_Bill_LastName", lastName);
//        }
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
        String queryUrl = query.toString();
//        String vnp_SecureHash = PaymentConfig.Sha256(PaymentConfig.CHECKSUM + hashData);
//        queryUrl += "&vnp_SecureHashType=SHA256&vnp_SecureHash=" + vnp_SecureHash;
        String vnp_SecureHash = PaymentConfig.hmacSHA512(PaymentConfig.CHECKSUM, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = PaymentConfig.VNPAYURL + "?" + queryUrl;

        PaymentResultDTO paymentResultDTO = new PaymentResultDTO();
        paymentResultDTO.setUrl(paymentUrl);
        paymentResultDTO.setStatus("00");
        paymentResultDTO.setMessage("Success");
        return ResponseEntity.status(HttpStatus.OK).body(paymentResultDTO.toString());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> paymentOrder(@PathVariable long id){
        OrderEntity order = orderService.findOrderById(id);
        if (order == null)
        {
            return new ResponseEntity<>("Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND);
        }
        // Lấy ra danh sách các sản phẩm trong order detail
        List<OrderDetailsEntity> orderDetailsEntityList = orderDetailService.findAllByOrderEntityId(id);
        // Duyệt qua từng sản phẩm này (Lấy ra productId, type, quantity),
        // sau đó tìm và cập nhật lại trong bảng type với productId, Type và Quantity tương ứng
        for (OrderDetailsEntity orderDetail : orderDetailsEntityList)
        {
            // Lấy ra product với id, type và quantity tương ứng
            TypeEntity type = typeRepository.findProductByType(orderDetail.getProductId(), orderDetail.getType());
            ProductEntity product = productRepository.findById(orderDetail.getProductId()).get();
            // Update type ở bảng type với quantity = quantity - orderDetail.quantity
            type.setQuantity(type.getQuantity() - orderDetail.getQuantity());
            typeRepository.save(type);
            // Update số lượng đã bán ở bảng Product
            product.setSold(product.getSold() + orderDetail.getQuantity());
            productRepository.save(product);
            // done
        }
        // Set trạng thái order thành "Đặt hàng thành công!"
        order.setStatus("DONE");
        orderService.updateOrder(order);
        return ResponseEntity.ok("Thanh toán thành công!");

    }


}

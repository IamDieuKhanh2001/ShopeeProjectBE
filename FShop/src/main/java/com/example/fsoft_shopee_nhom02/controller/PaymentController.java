package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.model.OrderDetailsEntity;
import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import com.example.fsoft_shopee_nhom02.model.ProductEntity;
import com.example.fsoft_shopee_nhom02.model.TypeEntity;
import com.example.fsoft_shopee_nhom02.repository.ProductRepository;
import com.example.fsoft_shopee_nhom02.repository.TypeRepository;
import com.example.fsoft_shopee_nhom02.service.OrderDetailService;
import com.example.fsoft_shopee_nhom02.service.impl.OrderDetailServiceImpl;
import com.example.fsoft_shopee_nhom02.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{id}")
    public ResponseEntity<?> paymentOrder(@PathVariable long id){
        OrderEntity order = orderService.findOrderById(id);
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
        }
        // Set trạng thái order thành "Đặt hàng thành công!"
        order.setStatus("Đặt hàng thành công!");
        return new ResponseEntity<>(order, HttpStatus.OK);
    }


}

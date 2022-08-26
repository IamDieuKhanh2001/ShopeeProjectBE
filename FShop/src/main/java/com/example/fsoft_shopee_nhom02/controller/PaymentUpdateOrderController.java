package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.config.GlobalVariable;
import com.example.fsoft_shopee_nhom02.dto.UpdateOrderDTO;
import com.example.fsoft_shopee_nhom02.service.impl.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentUpdateOrderController {
    @Autowired
    PaymentServiceImpl paymentService;

    @PutMapping("updateorder")
    public ResponseEntity<?> acceptOrder(@RequestBody UpdateOrderDTO updateOrderDTO){
        if (updateOrderDTO.getStatus().equals(GlobalVariable.ORDER_STATUS.CANCELED.toString()))
        {
            return paymentService.cancelOrder(updateOrderDTO.getId());
        }
        return paymentService.acceptOrder(updateOrderDTO.getId());
    }
}

package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.service.OrderDetailService;
import com.example.fsoft_shopee_nhom02.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Order")
@CrossOrigin
public class OrderController {
    private final OrderDetailService orderDetailService;
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderDetailService orderDetailService, OrderService orderService) {
        this.orderDetailService = orderDetailService;
        this.orderService = orderService;
    }

    @GetMapping("/All")
    public Object getAllOrder() {
        return orderService.getAll();
    }

    @GetMapping("/Detail")
    public Object getAllOrderDetail() {
        return orderDetailService.getAll();
    }

    @GetMapping("/{id}")
    public Object getAllOrderByUserId(@PathVariable String id) {
        return orderService.getAllByUserId(Long.parseLong(id));
    }

    @GetMapping("/Detail/{id}")
    public Object getOrderDetailByOrderId(@PathVariable String id) {
        return orderDetailService.getOrderDetailByOrderId(Long.parseLong(id));
    }

    @GetMapping("/GetOne/{id}")
    public Object getOneOrderById(@PathVariable String id) {
        return orderService.findOrderById(Long.parseLong(id));
    }

    @PostMapping("/CreateOrder")
    public Object CreateOrder() {
        return "created order";
    }

    @GetMapping("/Delete")
    public Object DeleteOrder() {
        return "deleted order";
    }
}

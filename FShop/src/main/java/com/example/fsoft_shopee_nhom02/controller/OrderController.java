package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.config.GlobalVariable;
import com.example.fsoft_shopee_nhom02.service.OrderDetailService;
import com.example.fsoft_shopee_nhom02.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {
    private final OrderDetailService orderDetailService;
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderDetailService orderDetailService, OrderService orderService) {
        this.orderDetailService = orderDetailService;
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public Object getAllOrder() {
        return orderService.getAll();
    }

    @GetMapping("/detail")
    public Object getAllOrderDetail() {
        return orderDetailService.getAll();
    }

    @GetMapping("/{id}")
    public Object getAllOrderByUserId(@PathVariable String id) {
        return orderService.getAllByUserId(Long.parseLong(id));
    }

    @GetMapping("/detail/{id}")
    public Object getOrderDetailByOrderId(@PathVariable String id) {
        return orderDetailService.getOrderDetailByOrderId(Long.parseLong(id));
//        return orderDetailService.findAllByOrderEntityId(Long.parseLong(id));
    }

    @GetMapping("/get_order/{id}")
    public Object getOneOrderById(@PathVariable String id) {
        return orderService.findOrderById(Long.parseLong(id));
    }

    @PostMapping("/create_order")
    public Object CreateOrder(@RequestBody Object req) {
        // create order detail Entities
        if (req.getClass() == ArrayList.class) {
            System.out.println("Array of Hash Map => Multi Order Product");
        } else {
            System.out.println("Hash Map => One Product Order");
        }

        // calculate for order Entity

        // create order Entity

        // insert order to DB

        // insert order detail to DB

        return "created order at " + GlobalVariable.datetimeFormat.format(new Date());
    }

    @PostMapping("/update_order")
    public Object UpdateOrder(@RequestBody Map<String, String> req) {
        System.out.println(req);
        return "updated order";
    }

    @PostMapping("/delete")
    public Object DeleteOrder(@RequestBody Map<String, String> req) {
        System.out.println(req);
        return "deleted order";
    }
}

package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.config.GlobalVariable;
import com.example.fsoft_shopee_nhom02.model.OrderDetailsEntity;
import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.service.OrderDetailService;
import com.example.fsoft_shopee_nhom02.service.OrderService;
import com.example.fsoft_shopee_nhom02.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {
    private final OrderDetailService orderDetailService;
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderDetailService orderDetailService, OrderService orderService, UserService userService) {
        this.orderDetailService = orderDetailService;
        this.orderService = orderService;
        this.userService = userService;
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
    public Object CreateOrder(@RequestBody List<Object> req) {
        // get request data
        List<Map<String, String>> orderDetailsEntityList_req = (List<Map<String, String>>) req.get(0);
        Map<String, String> orderInformation = (Map<String, String>) req.get(1);

        // create variable for usage
        OrderEntity orderEntity = new OrderEntity();
        List<OrderDetailsEntity> orderDetailsEntityList = new ArrayList<>();

        // analyze orderInformation
        UserEntity user = userService.findByIdUser(Long.parseLong(orderInformation.get("user_id")));
        String note=orderInformation.get("note");
        String payment=orderInformation.get("payment");
        long shipping_fee=Long.parseLong(orderInformation.get("shipping_fee"));

        // calculate for order Entity
        String created_date = GlobalVariable.datetimeFormat.format(new Date());
        String status = "Waiting for confirm";
        long total_cost = 0;

        // analyze order Product data
        for (Map<String, String> i : orderDetailsEntityList_req) {
            OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity();
            orderDetailsEntity.setQuantity(Long.parseLong(i.get("quantity")));
            orderDetailsEntity.setUnitPrice(Long.parseLong(i.get("unit_price")));
            orderDetailsEntity.setProductEntityID(Long.parseLong(i.get("product_id")));
            orderDetailsEntity.setType(i.get("type"));

            total_cost += Long.parseLong(i.get("unit_price")) * Long.parseLong(i.get("quantity"));

            orderDetailsEntityList.add(orderDetailsEntity);
        }

        System.out.println(orderInformation);

        // map value for orderEntity
//        orderEntity.setCreatedDate();

        // insert order to DB

        // insert order detail to DB

        return "created order at " + GlobalVariable.datetimeFormat.format(new Date());
    }

    @PostMapping("/update_order")
    public Object UpdateOrder(@RequestBody Map<String, String> req) {
        System.out.println(req);
        return "updated order";
    }

    @PostMapping("/cancel_order/{id}")
    public Object DeleteOrder(@PathVariable String id) {
        return "deleted order";
    }
}

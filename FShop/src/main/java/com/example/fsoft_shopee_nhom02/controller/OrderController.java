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

import java.sql.Timestamp;
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
        long startTime = System.currentTimeMillis();

        // get request data
        List<Map<String, String>> orderDetailsEntityList_req = (List<Map<String, String>>) req.get(0);
        Map<String, String> orderInformation = (Map<String, String>) req.get(1);

        // create variable for usage
        OrderEntity orderEntity = new OrderEntity();
        List<OrderDetailsEntity> orderDetailsEntityList = new ArrayList<>();

        // get user information
        UserEntity user = userService.findByIdUser(Long.parseLong(orderInformation.get("user_id")));

        // calculate for order Entity
        Timestamp created_date = new Timestamp(GlobalVariable.getCurrentDate().getTime());
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

        // map value for orderEntity
        orderEntity.setCreatedDate(created_date);
        orderEntity.setModifiedDate(created_date);

        if (orderInformation.get("address") == null) {
            orderEntity.setAddress(user.getAddressEntityList().get(0).getAddress());
            orderEntity.setPhone(user.getPhone());
            orderEntity.setUserName(user.getUsername());
        } else {
            orderEntity.setAddress(orderInformation.get("address"));
            orderEntity.setPhone(orderInformation.get("phone"));
            orderEntity.setUserName(orderInformation.get("user_name"));
        }

        orderEntity.setNote(orderInformation.get("note"));
        orderEntity.setPayment(orderInformation.get("payment"));
        orderEntity.setShippingFee(Long.parseLong(orderInformation.get("shipping_fee")));
        orderEntity.setStatus(status);
        orderEntity.setTotalPrice(total_cost);
        orderEntity.setUserId(user.getId());

        // insert order to DB
        orderService.addNewOrder(orderEntity);

        // insert order detail to DB
        for (OrderDetailsEntity i : orderDetailsEntityList) {
            i.setOrderEntityID(orderEntity.getId());
        }

        orderDetailService.addNewOrderDetails(orderDetailsEntityList);

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        return "created order at " + GlobalVariable.datetimeFormat.format(new Date()) + " with duration: " + totalTime + "ms";
    }

    @PostMapping("/update_order/{id}")
    public Object UpdateOrder(@RequestBody Map<String, String> req, @PathVariable String id) {
        System.out.println(req);
        return "updated order";
    }

    @PostMapping("/cancel_order/{id}")
    public Object DeleteOrder(@PathVariable String id) {
        return "deleted order";
    }
}

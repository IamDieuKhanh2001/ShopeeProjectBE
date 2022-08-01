package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.config.GlobalVariable;
import com.example.fsoft_shopee_nhom02.dto.AddressDTO;
import com.example.fsoft_shopee_nhom02.model.OrderDetailsEntity;
import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.service.AddressService;
import com.example.fsoft_shopee_nhom02.service.OrderDetailService;
import com.example.fsoft_shopee_nhom02.service.OrderService;
import com.example.fsoft_shopee_nhom02.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {
    private final OrderDetailService orderDetailService;
    private final OrderService orderService;
    private final UserService userService;
    private final AddressService addressService;

    @Autowired
    public OrderController(OrderDetailService orderDetailService, OrderService orderService, UserService userService, AddressService addressService) {
        this.orderDetailService = orderDetailService;
        this.orderService = orderService;
        this.userService = userService;
        this.addressService = addressService;
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
    }

    @GetMapping("/get_order/{id}")
    public Object getOneOrderById(@PathVariable String id) {
        return orderService.findOrderById(Long.parseLong(id));
    }

    @PostMapping("/create_order")
    public Object CreateOrder(@RequestBody List<Object> req) throws ParseException {
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
        Timestamp created_date = GlobalVariable.getCurrentDateTime();
        String status = "Waiting for confirm";
        AtomicLong total_cost = new AtomicLong();

        // analyze order Product data
        orderDetailsEntityList_req.forEach(i -> {
            OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity();
            orderDetailsEntity.setQuantity(Long.parseLong(i.get("quantity")));
            orderDetailsEntity.setUnitPrice(Long.parseLong(i.get("unit_price")));
            orderDetailsEntity.setProductEntityID(Long.parseLong(i.get("product_id")));
            orderDetailsEntity.setType(i.get("type"));

            total_cost.addAndGet(Long.parseLong(i.get("unit_price")) * Long.parseLong(i.get("quantity")));
            orderDetailsEntityList.add(orderDetailsEntity);
        });

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

            // add new address for later usage
            addressService.saveUserAddress(new AddressDTO(orderInformation.get("address"), orderInformation.get("user_name"), orderInformation.get("phone")), user.getUsername());
        }

        orderEntity.setNote(orderInformation.get("note"));
        orderEntity.setPayment(orderInformation.get("payment"));
        orderEntity.setShippingFee(Long.parseLong(orderInformation.get("shipping_fee")));
        orderEntity.setStatus(status);
        orderEntity.setTotalPrice(total_cost.get());
        orderEntity.setUserId(user.getId());

        // insert order to DB
        orderService.addNewOrder(orderEntity);

        // insert order detail to DB
        long newOrderEntityID = orderEntity.getId();
        orderDetailsEntityList.forEach(orderDetailsEntity -> orderDetailsEntity.setOrderEntityID(newOrderEntityID));
        orderDetailService.addNewOrderDetails(orderDetailsEntityList);

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        return "created order at " + GlobalVariable.datetimeFormat.format(new Date()) + " with duration: " + totalTime + "ms";
    }

    @PostMapping("/update_order/{id}")
    public Object UpdateOrder(@RequestBody Map<String, String> req, @PathVariable String id) {
        OrderEntity orderEntity = new OrderEntity();
        // add column those need to be updated
        for (Map.Entry<String, String> i : req.entrySet()) {
            System.out.println(i.getKey());
        }
        // orderService.updateOrder(orderEntity);
        return "updated order";
    }

    @PostMapping("/cancel_order/{id}")
    public Object DeleteOrder(@PathVariable String id) {
        orderService.deleteOrder(Long.parseLong(id));
        return "deleted order";
    }
}

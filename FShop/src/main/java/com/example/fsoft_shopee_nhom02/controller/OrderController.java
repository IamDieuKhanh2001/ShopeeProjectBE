package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.Notification.NotificationService;
import com.example.fsoft_shopee_nhom02.dto.AddressDTO;
import com.example.fsoft_shopee_nhom02.model.OrderDetailsEntity;
import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import com.example.fsoft_shopee_nhom02.model.ProductEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.example.fsoft_shopee_nhom02.config.GlobalVariable.*;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {
    private final OrderDetailService orderDetailService;
    private final OrderService orderService;
    private final UserService userService;
    private final AddressService addressService;
    private final CartProductService cartProductService;
    private final NotificationService notificationService;

    @Autowired
    public OrderController(OrderDetailService orderDetailService, OrderService orderService, UserService userService, AddressService addressService, CartProductService cartProductService, NotificationService notificationService) {
        this.orderDetailService = orderDetailService;
        this.orderService = orderService;
        this.userService = userService;
        this.addressService = addressService;
        this.cartProductService = cartProductService;
        this.notificationService = notificationService;
    }

    @PostMapping("/sentMessage")
    public void sentMessage(@RequestBody Map<String, String> message) {
        // more config
        //
        notificationService.sendNotification(message.get("message"), message.get("userId"));
    }

    @GetMapping("/all")
    public Object getAllOrder() {
        return orderService.getAll();
    }

    @GetMapping("/detail/all")
    public Object getAllOrderDetail() {
        return orderDetailService.getAll();
    }

    @GetMapping("/user/{id}")
    public Object getAllByUserId(@PathVariable String id) {
        return orderService.getAllByUserId(Long.parseLong(id));
    }

    @GetMapping("/pending/{id}")
    public Object getAllPendingOrderByUserId(@PathVariable String id) {
        return orderService.getAllPendingOrderByUserId(Long.parseLong(id), ORDER_STATUS.PENDING.toString());
    }

    @GetMapping("/history/{id}")
    public Object getAllOrderHistoryByUserId(@PathVariable String id) {
        return orderService.getAllHistoryOrderByUserId(Long.parseLong(id), ORDER_STATUS.PENDING.toString());
    }

    @GetMapping("/detail/{id}")
    public Object getOrderDetailByOrderId(@PathVariable String id) {
        return orderService.findById(Long.parseLong(id)).getOrderDetailsEntities();
    }

    @GetMapping("/get_order/{id}")
    public Object getOneOrderById(@PathVariable String id) {
        return orderService.findOrderById(Long.parseLong(id));
    }

    @PostMapping("/create_order")
    public Object CreateOrder(@RequestBody List<Object> req) throws ParseException {
        // get request data
        List<Map<String, String>> orderDetailsEntityList_req = (List<Map<String, String>>) req.get(0);
        Map<String, String> orderInformation = (Map<String, String>) req.get(1);
        // create variable for usage
        OrderEntity orderEntity;
        // get user information
        UserEntity user = userService.findByIdUser(Long.parseLong(orderInformation.get("user_id")));
        AtomicReference<Long> total_cost = new AtomicReference<>((long) 0);
        // analyze order Product data
        List<OrderDetailsEntity> orderDetailsEntityList = orderDetailsEntityList_req.stream().map(orderEntity_req -> {
            OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity(Long.parseLong(orderEntity_req.get("unit_price")), Long.parseLong(orderEntity_req.get("quantity")), orderEntity_req.get("type"));
            orderDetailsEntity.setProductEntityID(Long.parseLong(orderEntity_req.get("product_id")));
            total_cost.updateAndGet(v -> v + (Long.parseLong(orderEntity_req.get("unit_price")) * Long.parseLong(orderEntity_req.get("quantity"))));
            return orderDetailsEntity;
        }).collect(Collectors.toList());
        // map value for orderEntity and check if user want to add new address
        if (orderInformation.get("address") == null) {
            orderEntity = new OrderEntity(ORDER_STATUS.PENDING.toString(), user.getUsername(), user.getAddressEntityList().get(0).getAddress(), user.getPhone(), orderInformation.get("note"), orderInformation.get("payment"), Long.parseLong(orderInformation.get("shipping_fee")), total_cost.get(), user, getCurrentDateTime());
        } else {
            orderEntity = new OrderEntity(ORDER_STATUS.PENDING.toString(), orderInformation.get("user_name"), orderInformation.get("address"), orderInformation.get("phone"), orderInformation.get("note"), orderInformation.get("payment"), Long.parseLong(orderInformation.get("shipping_fee")), total_cost.get(), user, getCurrentDateTime());
            // add new address for later usage
            addressService.saveUserAddress(new AddressDTO(orderInformation.get("address"), orderInformation.get("user_name"), orderInformation.get("phone")), user.getUsername());
        }
        // map order and order-details
        orderEntity.setOrderDetailsEntities(orderDetailsEntityList);
        orderDetailsEntityList.forEach(i -> i.setOrderEntity(orderEntity));
        // insert order and details to DB
        orderService.addNewOrder(orderEntity);
        // delete cartProduct from DB
        cartProductService.deleteListOfCartProduct(orderDetailsEntityList.stream().map(OrderDetailsEntity::getProductId).collect(Collectors.toList()), user.getCartEntity().getId(), orderDetailsEntityList.stream().map(OrderDetailsEntity::getType).collect(Collectors.toList()));
        return "created order";
    }

    @PostMapping("/update_order/{id}")
    public Object UpdateOrder(@RequestBody Map<String, String> req, @PathVariable String id) throws ParseException {
        OrderEntity orderEntity = orderService.findById(Long.parseLong(id));

        orderEntity.setModifiedDate(getCurrentDateTime());

        req.forEach((key, value) -> {
            switch (key) {
                case "address":
                    orderEntity.setAddress(value);
                    break;
                case "note":
                    orderEntity.setNote(value);
                    break;
                case "payment":
                    orderEntity.setPayment(value);
                    break;
                case "phone":
                    orderEntity.setPhone(value);
                    break;
                case "shipping_fee":
                    orderEntity.setShippingFee(Long.parseLong(value));
                    break;
                case "status":
                    orderEntity.setStatus(value);
                    break;
                case "total_price":
                    orderEntity.setTotalPrice(Long.parseLong(value));
                    break;
                case "username":
                    orderEntity.setUserName(value);
                    break;
                default:
                    break;
            }
        });

        orderService.updateOrder(orderEntity);
        return "updated order";
    }

    @PostMapping("/cancel_order/{id}")
    public Object CancelOrder(@PathVariable String id) {
        OrderEntity orderEntity = orderService.findById(Long.parseLong(id));
        orderEntity.setStatus(ORDER_STATUS.CANCELED.toString());

        orderService.updateOrder(orderEntity);
        return "canceled order";
    }

    @GetMapping("/get_shipping_fee")
    public Object get_shipping_fee(@RequestParam String f, @RequestParam String t, @RequestParam String w) {
        String httpRequest = "Can't get Shipping fee, Server Busy";

        try {
            URL url = new URL("http://www.vnpost.vn/vi-vn/tra-cuu-gia-cuoc?from=" + f + "&to=" + t + "&weight=" + w);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            String line;
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            StringBuilder res_html = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                res_html.append(line);
            }
            bufferedReader.close();
            httpRequest = res_html.toString();
        } catch (Exception e) {
            System.out.println(httpRequest);
        }

        return httpRequest;
    }
}

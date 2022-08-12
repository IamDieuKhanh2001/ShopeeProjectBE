package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.Notification.NotificationService;
import com.example.fsoft_shopee_nhom02.dto.AddressDTO;
import com.example.fsoft_shopee_nhom02.model.OrderDetailsEntity;
import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private final CartService cartService;
    private final CartProductService cartProductService;
    private final NotificationService notificationService;

    @Autowired
    public OrderController(OrderDetailService orderDetailService, OrderService orderService, UserService userService, AddressService addressService, CartService cartService, CartProductService cartProductService, SimpMessagingTemplate simpMessagingTemplate, NotificationService notificationService) {
        this.orderDetailService = orderDetailService;
        this.orderService = orderService;
        this.userService = userService;
        this.addressService = addressService;
        this.cartService = cartService;
        this.cartProductService = cartProductService;
        this.notificationService = notificationService;
    }

    @GetMapping("/sentMessage")
    public void sentMessage(@RequestBody Map<String, String> message) {
        // more config

        //
        notificationService.sendNotification(message.get("message"),message.get("userId"));
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
    public Object getAllOrderByUserId(@PathVariable String id) {
        return orderService.getAllPendingOrderByUserId(Long.parseLong(id), ORDER_STATUS.PENDING.toString());
    }

    @GetMapping("/history/{id}")
    public Object getAllOrderHistoryByUserId(@PathVariable String id) {
        return orderService.getAllHistoryOrderByUserId(Long.parseLong(id), ORDER_STATUS.DONE.toString());
    }

    @GetMapping("/detail/{id}")
    public Object getOrderDetailByOrderId(@PathVariable String id) {
        List<OrderDetailsEntity> orderDetailsEntityList = orderDetailService.findAllByOrderEntityId(Long.parseLong(id));
        OrderEntity orderEntity = orderService.findById(Long.parseLong(id));

        Map<String, Object> res = new HashMap<>();

        res.put("orderDetailsList", orderDetailsEntityList);
        res.put("orderInfo", orderEntity);

        return res;
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
        OrderEntity orderEntity = new OrderEntity();
        List<OrderDetailsEntity> orderDetailsEntityList;
        Collection<Long> productEntity_idList;
        Collection<String> typeList;

        // get user information
        UserEntity user = userService.findByIdUser(Long.parseLong(orderInformation.get("user_id")));
        // get user cartService.
        long CardId = cartService.findByUserId(user.getId()).getId();

        // calculate for order Entity
        Timestamp created_date = getCurrentDateTime();
        String status = ORDER_STATUS.PENDING.toString();
        AtomicReference<Long> total_cost = new AtomicReference<>((long) 0);

        // analyze order Product data
        orderDetailsEntityList = orderDetailsEntityList_req.stream()
                .map(orderEntity_req -> {
                    OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity();
                    orderDetailsEntity.setQuantity(Long.parseLong(orderEntity_req.get("quantity")));
                    orderDetailsEntity.setUnitPrice(Long.parseLong(orderEntity_req.get("unit_price")));
                    orderDetailsEntity.setProductEntityID(Long.parseLong(orderEntity_req.get("product_id")));
                    orderDetailsEntity.setType(orderEntity_req.get("type"));

                    total_cost.updateAndGet(v -> v + (Long.parseLong(orderEntity_req.get("unit_price")) * Long.parseLong(orderEntity_req.get("quantity"))));
                    return orderDetailsEntity;
                }).collect(Collectors.toList());

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

        // insert orderId for orderDetails
        long newOrderEntityID = orderEntity.getId();

        // set attribute for delete cartProduct func
        productEntity_idList = orderDetailsEntityList.stream().map(OrderDetailsEntity::getProductId).collect(Collectors.toList());
        typeList = orderDetailsEntityList.stream().map(OrderDetailsEntity::getType).collect(Collectors.toList());

        // set id for order Detail
        orderDetailsEntityList.forEach(orderDetailsEntity -> orderDetailsEntity.setOrderEntityID(newOrderEntityID));

        // insert order detail to DB
        orderDetailService.addNewOrderDetails(orderDetailsEntityList);

        // delete cartProduct from DB
        cartProductService.deleteListOfCartProduct(productEntity_idList, CardId, typeList);

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

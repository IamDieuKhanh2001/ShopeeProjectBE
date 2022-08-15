package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.Notification.NotificationService;
import com.example.fsoft_shopee_nhom02.dto.AddressDTO;
import com.example.fsoft_shopee_nhom02.model.OrderDetailsEntity;
import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

    @GetMapping("/all/{page}")
    public Object getAllOrder(@PathVariable String page) {
        Map<String, Object> res = new HashMap<>();
        Page<OrderEntity> orderEntityPage = orderService.getAllPaging(Integer.parseInt(page) - 1);

        res.put("orderEntityPage", orderEntityPage.getContent());
        res.put("maxPage", orderEntityPage.getTotalPages());

        return res;
    }

    @GetMapping("/detail/all/{page}")
    public Object getAllOrderDetail(@PathVariable String page) {
        Map<String, Object> res = new HashMap<>();
        Page<OrderDetailsEntity> orderDetailsEntityPage = orderDetailService.getAllPaging(Integer.parseInt(page) - 1);

        res.put("orderDetailsEntityPage", orderDetailsEntityPage.getContent());
        res.put("maxPage", orderDetailsEntityPage.getTotalPages());

        return res;
    }

    @GetMapping("/all/{status}/{page}")
    public Object getAllOrderByStatus(@PathVariable String status, @PathVariable String page) {
        Map<String, Object> res = new HashMap<>();
        Page<OrderEntity> orderEntityPage = orderService.getAllByStatus(status, Integer.parseInt(page) - 1);

        res.put("orderEntityPage", orderEntityPage.getContent());
        res.put("maxPage", orderEntityPage.getTotalPages());

        return res;
    }

    @GetMapping("/detail/all/{status}/{page}")
    public Object getAllOrderDetailByStatus(@PathVariable String status, @PathVariable String page) {
        Map<String, Object> res = new HashMap<>();
        Page<OrderDetailsEntity> orderDetailsEntityPage = orderDetailService.getAllByOrderStatus(status, Integer.parseInt(page) - 1);

        res.put("orderDetailsEntityPage", orderDetailsEntityPage.getContent());
        res.put("maxPage", orderDetailsEntityPage.getTotalPages());

        return res;
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
    public Object createOrder(@RequestBody List<Object> req) throws ParseException {
        // get request data
        List<Map<String, String>> orderDetailsEntityListReq = (List<Map<String, String>>) req.get(0);
        Map<String, String> orderInformation = (Map<String, String>) req.get(1);
        // create variable for usage
        OrderEntity orderEntity;
        // get user information
        UserEntity user = userService.findByIdUser(Long.parseLong(orderInformation.get("user_id")));
        AtomicReference<Long> totalCost = new AtomicReference<>((long) 0);
        // analyze order Product data
        List<OrderDetailsEntity> orderDetailsEntityList = orderDetailsEntityListReq.stream().map(orderEntityReq -> {
            OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity(Long.parseLong(orderEntityReq.get("unit_price")), Long.parseLong(orderEntityReq.get("quantity")), orderEntityReq.get("type"));
            orderDetailsEntity.setProductEntityID(Long.parseLong(orderEntityReq.get("product_id")));
            totalCost.updateAndGet(v -> v + (Long.parseLong(orderEntityReq.get("unit_price")) * Long.parseLong(orderEntityReq.get("quantity"))));
            return orderDetailsEntity;
        }).collect(Collectors.toList());
        // map value for orderEntity and check if user want to add new address
        if (orderInformation.get("address") == null) {
            orderEntity = new OrderEntity(ORDER_STATUS.PENDING.toString(), user.getUsername(), user.getAddressEntityList().get(0).getAddress(), user.getPhone(), orderInformation.get("note"), orderInformation.get("payment"), Long.parseLong(orderInformation.get("shipping_fee")), totalCost.get(), user, getCurrentDateTime());
        } else {
            orderEntity = new OrderEntity(ORDER_STATUS.PENDING.toString(), orderInformation.get("user_name"), orderInformation.get("address"), orderInformation.get("phone"), orderInformation.get("note"), orderInformation.get("payment"), Long.parseLong(orderInformation.get("shipping_fee")), totalCost.get(), user, getCurrentDateTime());
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
    public Object updateOrder(@RequestBody Map<String, String> req, @PathVariable String id) throws ParseException {
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

    @PostMapping("/cancel_order")
    public Object cancelOrder(@RequestBody Map<String, String> req) {
        OrderEntity orderEntity = orderService.findById(Long.parseLong(req.get("id")));
        orderEntity.setStatus(ORDER_STATUS.CANCELED.toString());

        orderService.updateOrder(orderEntity);
        return "canceled order";
    }

    @GetMapping("/get_shipping_fee")
    public Object getShippingFee(@RequestParam String f, @RequestParam String t, @RequestParam String w) {
        String httpRequest = "Can't get Shipping fee, Server Busy";

        try {
            URL url = new URL("http://www.vnpost.vn/vi-vn/tra-cuu-gia-cuoc?from=" + f + "&to=" + t + "&weight=" + w);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            String line;
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            StringBuilder resHtml = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                resHtml.append(line);
            }
            bufferedReader.close();
            httpRequest = resHtml.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return httpRequest;
    }
}

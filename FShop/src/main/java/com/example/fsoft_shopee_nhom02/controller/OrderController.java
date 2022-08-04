package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.config.GlobalVariable;
import com.example.fsoft_shopee_nhom02.dto.AddressDTO;
import com.example.fsoft_shopee_nhom02.model.OrderDetailsEntity;
import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.service.*;
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
    private final CartService cartService;
    private final CartProductService cartProductService;

    @Autowired
    public OrderController(OrderDetailService orderDetailService, OrderService orderService, UserService userService, AddressService addressService, CartService cartService, CartProductService cartProductService) {
        this.orderDetailService = orderDetailService;
        this.orderService = orderService;
        this.userService = userService;
        this.addressService = addressService;
        this.cartService = cartService;
        this.cartProductService = cartProductService;
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
        return orderService.getAllPendingOrderByUserId(Long.parseLong(id), GlobalVariable.ORDER_STATUS_DONE);
    }

    @GetMapping("/history/{id}")
    public Object getAllOrderHistoryByUserId(@PathVariable String id) {
        return orderService.getAllHistoryOrderByUserId(Long.parseLong(id), GlobalVariable.ORDER_STATUS_DONE);
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
        long startTime = System.currentTimeMillis();

        // get request data
        List<Map<String, String>> orderDetailsEntityList_req = (List<Map<String, String>>) req.get(0);
        Map<String, String> orderInformation = (Map<String, String>) req.get(1);

        // create variable for usage
        OrderEntity orderEntity = new OrderEntity();
        List<OrderDetailsEntity> orderDetailsEntityList = new ArrayList<>();
        Collection<Long> productEntity_idList = new ArrayList<>();
        Collection<String> typeList = new ArrayList<>();

        // get user information
        UserEntity user = userService.findByIdUser(Long.parseLong(orderInformation.get("user_id")));
        // get user cartService.
        long CardId = cartService.findByUserId(user.getId()).getId();

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

        // insert orderId for orderDetails
        long newOrderEntityID = orderEntity.getId();
        orderDetailsEntityList.forEach(
                (orderDetailsEntity) -> {
                    // set id for order Detail
                    orderDetailsEntity.setOrderEntityID(newOrderEntityID);

                    // set attribute for delete cartProduct func
                    productEntity_idList.add(orderDetailsEntity.getProductId());
                    typeList.add(orderDetailsEntity.getType());
                }
        );

        // insert order detail to DB
        orderDetailService.addNewOrderDetails(orderDetailsEntityList);

        // delete cartProduct from DB
        cartProductService.deleteListOfCartProduct(productEntity_idList, CardId, typeList);

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        return "created order at " + GlobalVariable.datetimeFormat.format(new Date()) + " with duration: " + totalTime + "ms";
    }

    @PostMapping("/update_order/{id}")
    public Object UpdateOrder(@RequestBody Map<String, String> req, @PathVariable String id) throws ParseException {
        OrderEntity orderEntity = orderService.findById(Long.parseLong(id));

        orderEntity.setModifiedDate(GlobalVariable.getCurrentDateTime());

        for (Map.Entry<String, String> i : req.entrySet()) {
            System.out.println(i.getKey());
            switch (i.getKey()) {
                case "address":
                    orderEntity.setAddress(i.getValue());
                    break;
                case "note":
                    orderEntity.setNote(i.getValue());
                    break;
                case "payment":
                    orderEntity.setPayment(i.getValue());
                    break;
                case "phone":
                    orderEntity.setPhone(i.getValue());
                    break;
                case "shipping_fee":
                    orderEntity.setShippingFee(Long.parseLong(i.getValue()));
                    break;
                case "status":
                    orderEntity.setStatus(i.getValue());
                    break;
                case "total_price":
                    orderEntity.setTotalPrice(Long.parseLong(i.getValue()));
                    break;
                case "username":
                    orderEntity.setUserName(i.getValue());
                    break;
                default:
                    break;
            }
        }

        orderService.updateOrder(orderEntity);
        return "updated order";
    }

    @PostMapping("/cancel_order/{id}")
    public Object CancelOrder(@PathVariable String id) {
        OrderEntity orderEntity = orderService.findById(Long.parseLong(id));

        orderEntity.setStatus(GlobalVariable.ORDER_STATUS_CANCELED);

        orderService.updateOrder(orderEntity);
        return "canceled order";
    }
}

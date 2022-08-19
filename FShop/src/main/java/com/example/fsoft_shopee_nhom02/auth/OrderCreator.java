package com.example.fsoft_shopee_nhom02.auth;

import com.example.fsoft_shopee_nhom02.config.GlobalVariable;
import com.example.fsoft_shopee_nhom02.dto.AddressDTO;
import com.example.fsoft_shopee_nhom02.model.OrderDetailsEntity;
import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.service.AddressService;
import com.example.fsoft_shopee_nhom02.service.CartProductService;
import com.example.fsoft_shopee_nhom02.service.OrderService;
import com.example.fsoft_shopee_nhom02.service.UserService;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.example.fsoft_shopee_nhom02.config.GlobalVariable.getCurrentDateTime;

public class OrderCreator {
    public static OrderEntity CreateOrder(UserService userService, AddressService addressService, OrderService orderService, CartProductService cartProductService, List<Object> req) throws ParseException {
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
            orderEntity = new OrderEntity(GlobalVariable.ORDER_STATUS.PENDING.toString(), user.getUsername(), user.getAddressEntityList().get(0).getAddress(), user.getPhone(),  orderInformation.get("note") == null ? "" : orderInformation.get("note"), orderInformation.get("payment") == null ? "COD" : orderInformation.get("payment"), Long.parseLong(orderInformation.get("shipping_fee") == null ? "25000" : orderInformation.get("shipping_fee")), totalCost.get(), user, getCurrentDateTime());
        } else {
            orderEntity = new OrderEntity(GlobalVariable.ORDER_STATUS.PENDING.toString(), orderInformation.get("user_name"), orderInformation.get("address"), orderInformation.get("phone"), orderInformation.get("note") == null ? "" : orderInformation.get("note"), orderInformation.get("payment") == null ? "COD" : orderInformation.get("payment"), Long.parseLong(orderInformation.get("shipping_fee") == null ? "25000" : orderInformation.get("shipping_fee")), totalCost.get(), user, getCurrentDateTime());
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
        return orderEntity;
    }
}

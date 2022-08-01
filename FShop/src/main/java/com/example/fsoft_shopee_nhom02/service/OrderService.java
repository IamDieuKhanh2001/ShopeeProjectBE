package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.model.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderEntity> getAll();

    List<OrderEntity> getAllByUserId(long UserId);

    OrderEntity findOrderById(Long OrderId);

    void addNewOrder(OrderEntity orderEntity);

    OrderEntity findById(Long OrderId);

    void updateOrder(OrderEntity orderEntity);

    void deleteOrder(Long OrderId);
}

package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderEntity> getAll();

    List<OrderEntity> getAllByUserId(Long UserId);

    List<OrderEntity> getAllPendingOrderByUserId(Long UserId, String Status);

    List<OrderEntity> getAllHistoryOrderByUserId(Long UserId, String Status);

    OrderEntity findOrderById(Long OrderId);

    String getAllOrderByMonth(String Month);

    void addNewOrder(OrderEntity orderEntity);

    long getTurnOver();

    String getAllOrderByDay(String Year, String Month, String Day);

    long getTotalOrder();

    OrderEntity findById(Long OrderId);

    void updateOrder(OrderEntity orderEntity);

    void deleteOrder(Long OrderId);
}

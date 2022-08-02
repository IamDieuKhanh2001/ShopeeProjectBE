package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderEntity> getAll();

    List<OrderEntity> getAllByUserId(long UserId);

    Optional<OrderEntity> findOrderById(Long OrderId);

    long getAllOrderByMonth(String Month);
    void addNewOrder(OrderEntity orderEntity);
    long getTurnOver();
    long getAllOrderByDay(String Year,String Month,String Day);
    long getTotalOrder();
}

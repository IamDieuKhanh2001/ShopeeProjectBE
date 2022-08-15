package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Page<OrderEntity> getAll(int page);

    List<OrderEntity> getAllByUserId(Long UserId);

    List<OrderEntity> getAllPendingOrderByUserId(Long UserId, String Status);

    List<OrderEntity> getAllHistoryOrderByUserId(Long UserId, String Status);

    Page<OrderEntity> getAllByStatus(String status,int page);

    OrderEntity findOrderById(Long OrderId);

    String getAllOrderByMonth(String Month);

    void addNewOrder(OrderEntity orderEntity);

    long getTurnOver();

    String getAllOrderByDay(String Year, String Month, String Day);

    long getTotalOrder();

    OrderEntity findById(Long OrderId);

    void updateOrder(OrderEntity orderEntity);

    String getAllOrderFromDayToDay(String Year, String Month, String Day, String Year2, String Month2, String Day2);

    void deleteOrder(Long OrderId);
}

package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    Page<OrderEntity> getAllPaging(int page);

    Page<OrderEntity> getAllByUserId(Long UserId,int page);

    List<OrderEntity> getAllPendingOrderByUserId(Long UserId, String Status);

    List<OrderEntity> getAllHistoryOrderByUserId(Long UserId, String Status);

    Page<OrderEntity> getAllByStatus(String status,int page);

    OrderEntity findOrderById(Long OrderId);

    String getAllOrderByMonth(String Month);

    void addNewOrder(OrderEntity orderEntity);

    String getTurnOver();

    String getAllOrderByDay(String Year, String Month, String Day);

    String getTotalOrder();

    OrderEntity findById(Long OrderId);

    void updateOrder(OrderEntity orderEntity);

    String getAllOrderFromDayToDay(String DateStart, String DateEnd);

    void deleteOrder(Long OrderId);

    String getTotalProduct();

    String getTotalUsers();
}

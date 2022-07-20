package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderService {
    List<OrderEntity> findAll();
    void deleteById(Long id);
    OrderEntity save(OrderEntity orderEntity);
    Optional<OrderEntity> findById(Long id);
}

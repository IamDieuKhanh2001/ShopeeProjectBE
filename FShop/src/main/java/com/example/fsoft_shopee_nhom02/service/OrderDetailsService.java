package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.model.OrderDetailsEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderDetailsService {
    List<OrderDetailsEntity> findAll();
    void deleteById(Long id);
    OrderDetailsEntity save(OrderDetailsEntity orderDetailsEntity);
    Optional<OrderDetailsEntity> findById(Long id);
}

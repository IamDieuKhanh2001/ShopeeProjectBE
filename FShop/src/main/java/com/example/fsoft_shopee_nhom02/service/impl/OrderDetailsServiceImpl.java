package com.example.fsoft_shopee_nhom02.service.impl;

import com.example.fsoft_shopee_nhom02.model.OrderDetailsEntity;
import com.example.fsoft_shopee_nhom02.repository.OrderDetailsRepository;
import com.example.fsoft_shopee_nhom02.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class OrderDetailsServiceImpl implements OrderDetailsService {
    @Autowired
    OrderDetailsRepository orderDetailsRepository;


    @Override
    public List<OrderDetailsEntity> findAll() {
        return orderDetailsRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        orderDetailsRepository.deleteById(id);
    }

    @Override
    public OrderDetailsEntity save(OrderDetailsEntity orderDetailsEntity) {
        return orderDetailsRepository.save(orderDetailsEntity);
    }

    @Override
    public Optional<OrderDetailsEntity> findById(Long id) {
        return orderDetailsRepository.findById(id);
    }
}

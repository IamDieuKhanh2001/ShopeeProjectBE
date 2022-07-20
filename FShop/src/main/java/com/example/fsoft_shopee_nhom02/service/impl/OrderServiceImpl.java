package com.example.fsoft_shopee_nhom02.service.impl;

import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import com.example.fsoft_shopee_nhom02.repository.OrderRepository;
import com.example.fsoft_shopee_nhom02.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderEntity> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<OrderEntity> getAllByUserId(long UserId) {
        return orderRepository.findAllByUserEntitiesId(UserId);
    }

    @Override
    public Optional<OrderEntity> findOrderById(Long OrderId) {
        return orderRepository.findById(OrderId);
    }
}

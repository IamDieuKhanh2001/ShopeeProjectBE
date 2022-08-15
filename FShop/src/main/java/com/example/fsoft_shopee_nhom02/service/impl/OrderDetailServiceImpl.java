package com.example.fsoft_shopee_nhom02.service.impl;

import com.example.fsoft_shopee_nhom02.model.OrderDetailsEntity;
import com.example.fsoft_shopee_nhom02.repository.OrderDetailRepository;
import com.example.fsoft_shopee_nhom02.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.fsoft_shopee_nhom02.config.GlobalVariable.OrderPagingLimit;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public Page<OrderDetailsEntity> getAllByOrderStatus(String status, int page) {
        return orderDetailRepository.findAllByOrderEntityStatus(status, PageRequest.of(page, OrderPagingLimit));
    }

    @Override
    public Page<OrderDetailsEntity> getAll(int page) {
        return orderDetailRepository.findAll(PageRequest.of(page, OrderPagingLimit));
    }

    @Override
    public List<OrderDetailsEntity> findAllByOrderEntityId(Long OrderDetailId) {
        return orderDetailRepository.findAllByOrderEntityId(OrderDetailId);
    }
}

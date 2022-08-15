package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.model.OrderDetailsEntity;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface OrderDetailService {
    Page<OrderDetailsEntity> getAllByOrderStatus(String status,int page);

    Page<OrderDetailsEntity> getAll(int page);

    List<OrderDetailsEntity> findAllByOrderEntityId(Long OrderDetailId);
}

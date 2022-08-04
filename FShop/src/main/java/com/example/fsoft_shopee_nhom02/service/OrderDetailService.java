package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.model.OrderDetailsEntity;

import java.util.ArrayList;
import java.util.List;

public interface OrderDetailService {
    List<OrderDetailsEntity> getAll();

    List<OrderDetailsEntity> findAllByOrderEntityId(Long OrderDetailId);

    List<Object>  findByOrderId(Long OrderId);

    void addNewOrderDetails(List<OrderDetailsEntity> orderDetailsEntityList);

}

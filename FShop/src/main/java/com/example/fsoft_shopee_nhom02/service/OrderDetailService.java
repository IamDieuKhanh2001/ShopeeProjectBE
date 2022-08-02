package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.model.OrderDetailsEntity;

import java.util.ArrayList;
import java.util.List;

public interface OrderDetailService {
    List<OrderDetailsEntity> getAll();

    Object getOrderDetailByOrderId(Long OrderId);

    ArrayList<OrderDetailsEntity> findAllByOrderEntityId(Long OrderDetailId);

    void addNewOrderDetails(List<OrderDetailsEntity> orderDetailsEntityList);

}

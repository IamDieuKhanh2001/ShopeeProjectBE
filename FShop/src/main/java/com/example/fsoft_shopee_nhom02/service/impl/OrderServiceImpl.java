package com.example.fsoft_shopee_nhom02.service.impl;

import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import com.example.fsoft_shopee_nhom02.repository.OrderRepository;
import com.example.fsoft_shopee_nhom02.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.fsoft_shopee_nhom02.config.GlobalVariable.OrderPagingLimit;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    @Override
    public Page<OrderEntity> getAllByStatus(String status, int page) {
        return orderRepository.searchAllByStatus(status, PageRequest.of(page, OrderPagingLimit));
    }

    @Override
    public Page<OrderEntity> getAll(int page) {
        return orderRepository.findAll(PageRequest.of(page, OrderPagingLimit));
    }

    @Override
    public List<OrderEntity> getAllByUserId(Long UserId) {
        return orderRepository.findAllByUserEntitiesId(UserId);
    }

    @Override
    public List<OrderEntity> getAllPendingOrderByUserId(Long UserId, String Status) {
        return orderRepository.getAllByUserEntitiesIdAndStatus(UserId, Status);
    }

    @Override
    public List<OrderEntity> getAllHistoryOrderByUserId(Long UserId, String Status) {
        return orderRepository.getAllByUserEntitiesIdAndStatusNot(UserId, Status);
    }

    @Override
    public OrderEntity findOrderById(Long OrderId) {
        return orderRepository.searchById(OrderId);
    }

    @Override
    public String getAllOrderByMonth(String MonthM) {
        return orderRepository.getAllOrderByMonth(MonthM);
    }

    @Override
    public void addNewOrder(OrderEntity orderEntity) {
        orderRepository.save(orderEntity);
    }

    @Override
    public long getTurnOver() {
        return orderRepository.getTurnOver();
    }

    @Override
    public String getAllOrderByDay(String Year, String Month, String Day) {
        return orderRepository.getAllOrderByDay(Year, Month, Day);
    }

    @Override
    public long getTotalOrder() {
        return orderRepository.getTotalOrder();
    }

    @Override
    public OrderEntity findById(Long OrderId) {
        return orderRepository.searchById(OrderId);
    }

    @Override
    public void updateOrder(OrderEntity orderEntity) {
        orderRepository.saveAndFlush(orderEntity);
    }

    @Override
    public void deleteOrder(Long OrderId) {
        orderRepository.deleteById(OrderId);
    }

    public String getAllOrderFromDayToDay(String Year, String Month, String Day, String YearEnd, String MonthEnd, String DayEnd) {
        return orderRepository.getAllOrderFromDayToDay(Year, Month, Day, YearEnd, MonthEnd, DayEnd);
    }

}

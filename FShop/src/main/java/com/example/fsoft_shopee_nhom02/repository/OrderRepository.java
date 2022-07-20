package com.example.fsoft_shopee_nhom02.repository;

import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {

    List<OrderEntity> findAllByUserEntitiesId(Long UserId);
    
}

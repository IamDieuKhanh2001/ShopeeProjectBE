package com.example.fsoft_shopee_nhom02.repository;

import com.example.fsoft_shopee_nhom02.model.OrderDetailsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailsEntity, Long> {
    List<OrderDetailsEntity> findAllByOrderEntityId(Long OrderDetailId);

    Page<OrderDetailsEntity> findAllByOrderEntityStatusOrderByOrderEntityDesc(String orderEntity_status, Pageable pageable);

    Page<OrderDetailsEntity> findAllByOrderByOrderEntityDesc(Pageable pageable);
}

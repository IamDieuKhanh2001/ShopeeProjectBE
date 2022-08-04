package com.example.fsoft_shopee_nhom02.repository;

import com.example.fsoft_shopee_nhom02.model.OrderDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailsEntity, Long> {


    @Query(value = "select o.id as orderid,\n" +
            "       o.address,\n" +
            "       o.created_date,\n" +
            "       o.modified_date,\n" +
            "       o.payment,\n" +
            "       o.phone,\n" +
            "       o.shipping_fee,\n" +
            "       o.total_price,\n" +
            "       o.note,\n" +
            "       o.status,\n" +
            "       o.user_name,\n" +
            "       od.quantity,\n" +
            "       od.unit_price,\n" +
            "       od.type,\n" +
            "o.total_price+o.shipping_fee as total_pay" +
            "            from orders o\n" +
            "                     left join orderdetails od on o.id = od.order_id\n" +
            "            where od.order_id=:OrderId", nativeQuery = true)
    List<Object> findByOrderId(@Param("OrderId") Long OrderId);

    List<OrderDetailsEntity> findAllByOrderEntityId(Long OrderDetailId);
}

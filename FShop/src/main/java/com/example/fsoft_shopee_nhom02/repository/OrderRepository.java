package com.example.fsoft_shopee_nhom02.repository;

import com.example.fsoft_shopee_nhom02.model.OrderDetailsEntity;
import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {

    List<OrderEntity> findAllByUserEntitiesId(Long UserId);
    // Thống kê doanh thu theo tháng
    @Query(value = "select sum(total_price) \n" +
            "from orders \n" +
            "where year(modified_date) = '2022' and month(modified_date) =:Month and status = 'Done'", nativeQuery = true)
    long getAllOrderByMonth(@Param("Month") String Month);

    // Tổng doanh thu
    @Query(value = "select sum(total_price) \n" +
            "from orders where status = 'Done'\n" , nativeQuery = true)
    long getTurnOver();

    // Thống kê doanh thu theo ngày
    @Query(value = "select sum(total_price) \n" +
            "from orders \n" +
            "where year(modified_date) = :Year and month(modified_date) =:Month and day(modified_date) =:Day and status = 'Done'", nativeQuery = true)
    long getAllOrderByDay(@Param("Year") String Year, @Param("Month") String Month, @Param("Day") String Day);

    // Thống kê tổng số đơn hàng
    @Query(value = "SELECT count(*) \n" +
            "FROM shopee.orders where status = 'Done'", nativeQuery = true)
    long getTotalOrder();

    OrderEntity searchById(Long OrderId);
}

package com.example.fsoft_shopee_nhom02.repository;

import com.example.fsoft_shopee_nhom02.model.OrderDetailsEntity;
import com.example.fsoft_shopee_nhom02.model.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Page<OrderEntity> findAllByUserEntitiesId(Long UserId, Pageable pageable);

    List<OrderEntity> getAllByUserEntitiesIdAndStatus(Long userEntities_id, String status);

    List<OrderEntity> getAllByUserEntitiesIdAndStatusNot(Long userEntities_id, String status);

    Page<OrderEntity> searchAllByStatusOrderByCreatedDateDesc(String status, Pageable pageable);

    Page<OrderEntity> findAllByOrderByCreatedDateDesc(Pageable pageable);

    // Statistic by month
    @Query(value = "select sum(total_price) \n" +
            "from orders \n" +
            "where year(modified_date) = YEAR(CURDATE()) and month(modified_date) =:MonthM and status = 'Done'", nativeQuery = true)
    String getAllOrderByMonth(@Param("MonthM") String MonthM);

    // Total money
    @Query(value = "select sum(total_price) \n" +
            "from orders where status = 'Done' and year(modified_date) = YEAR(CURDATE())\n", nativeQuery = true)
    String getTurnOver();

    // Statistic by day
    @Query(value = "select sum(total_price) \n" +
            "from orders \n" +
            "where year(modified_date) = :Year and month(modified_date) =:Month and day(modified_date) =:Day and status = 'Done'", nativeQuery = true)
    String getAllOrderByDay(@Param("Year") String Year, @Param("Month") String Month, @Param("Day") String Day);

    // Total order with status = done
    @Query(value = "SELECT count(*) \n" +
            "FROM orders where status = 'Done'", nativeQuery = true)
    String getTotalOrder();

    OrderEntity searchById(Long OrderId);

    // Statistic from day to day
    @Query(value = "select sum(total_price)\n" +
            "from orders\n" +
            "where status = 'Done'\n" +
            "  and modified_date BETWEEN :DateStart AND :DateEnd", nativeQuery = true)
    String getAllOrderFromDayToDay(@Param("DateStart") String DayStart, @Param("DateEnd") String DateEnd);

    // Total order with status = done
    @Query(value = "SELECT count(*) \n" +
            "FROM products", nativeQuery = true)
    String getTotalProduct();


    // Total money
    @Query(value = "select sum(total_price) \n" +
            "from orders where status = 'Done'\n", nativeQuery = true)
    String getTotalSales();

    // Total user
    @Query(value = "Select count(*) from users join users_roles where users.id = users_roles.userid and users_roles.role_id = 2", nativeQuery = true)
    String getTotalUsers();
}

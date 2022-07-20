package com.example.fsoft_shopee_nhom02.repository;

import com.example.fsoft_shopee_nhom02.model.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity,Long> {
    List<AddressEntity> findByUserEntityId(Long userId);
}

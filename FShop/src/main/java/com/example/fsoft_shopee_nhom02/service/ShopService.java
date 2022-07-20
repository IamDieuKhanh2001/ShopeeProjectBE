package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.dto.ShopDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShopService {
    ShopDTO save (ShopDTO shopDTO);
    ShopDTO update (ShopDTO shopDTO);
    void delete(long id);
    List<ShopDTO> findAllShop();
    ShopDTO findShopById(long id);
    long countAllShop();
    ShopDTO searchByName(String keyword);

}

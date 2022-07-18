package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.dto.ShopDTO;
import org.springframework.stereotype.Service;

@Service
public interface ShopService {
    ShopDTO save (ShopDTO shopDTO);
}

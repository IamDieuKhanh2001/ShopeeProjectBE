package com.example.fsoft_shopee_nhom02.mapper;


import com.example.fsoft_shopee_nhom02.dto.ShopDTO;
import com.example.fsoft_shopee_nhom02.model.ShopEntity;

public class ShopMapper {

    public static ShopDTO toShopDto (ShopEntity shop){
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setId(shop.getId());
        shopDTO.setName(shop.getName());
        shopDTO.setAvatar(shop.getAvatar());
        shopDTO.setBackground(shop.getBackground());
        shopDTO.setDescription(shop.getDescription());
        shopDTO.setStatus(shop.getStatus());
        shopDTO.setTotalProduct(shop.getTotalProduct());

        return shopDTO;
    }

    public static ShopEntity toEntity (ShopDTO shopDTO){
        ShopEntity shop = new ShopEntity();
        shop.setId(shopDTO.getId());
        shop.setName(shopDTO.getName());
        shop.setAvatar(shopDTO.getAvatar());
        shop.setBackground(shopDTO.getBackground());
        shop.setDescription(shopDTO.getDescription());
        shop.setStatus(shopDTO.getStatus());
        shop.setTotalProduct(shopDTO.getTotalProduct());

        return shop;
    }
}

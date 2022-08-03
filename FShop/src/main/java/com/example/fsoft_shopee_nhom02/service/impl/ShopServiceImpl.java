package com.example.fsoft_shopee_nhom02.service.impl;

import com.example.fsoft_shopee_nhom02.dto.ShopDTO;
import com.example.fsoft_shopee_nhom02.exception.BadRequest;
import com.example.fsoft_shopee_nhom02.exception.NotFoundException;
import com.example.fsoft_shopee_nhom02.mapper.ShopMapper;
import com.example.fsoft_shopee_nhom02.model.ShopEntity;
import com.example.fsoft_shopee_nhom02.repository.ShopRepository;
import com.example.fsoft_shopee_nhom02.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopRepository shopRepository;

    @Override
    public ShopDTO save(ShopDTO shopDTO) {
        ShopEntity shop = ShopMapper.toEntity(shopDTO);
        shop = shopRepository.save(shop);
        return ShopMapper.toShopDto(shop);
    }

    @Override
    public ShopDTO update(ShopDTO shopDTO) {
        ShopEntity shop = shopRepository.findById(shopDTO.getId()).orElseThrow(()
        -> new BadRequest("Not found shop with id ="+shopDTO.getId()));

        shop.setName(shopDTO.getName());
        shop.setAvatar(shopDTO.getAvatar());
        shop.setBackground(shopDTO.getBackground());
        shop.setDescription(shopDTO.getDescription());
        shop.setStatus(shopDTO.getStatus());
        shop.setTotalProduct(shopDTO.getTotalProduct());

        shop = shopRepository.save(shop);

        return ShopMapper.toShopDto(shop);
    }

    @Override
    public void delete(long id) {
        ShopEntity shop = shopRepository.findById(id).orElseThrow(()
                -> new BadRequest("This shop not exist"));
        shopRepository.deleteById(id);
    }

    @Override
    public List<ShopDTO> findAllShop() {
        List<ShopDTO>shopDTOS = new ArrayList<>();
        List<ShopEntity> shops = shopRepository.findAll();
        for(ShopEntity shop : shops){
            shopDTOS.add(ShopMapper.toShopDto(shop));
        }
        if(shopDTOS.isEmpty()){
            throw  new NotFoundException("Empty!!");
        }
        return shopDTOS;
    }

    @Override
    public ShopDTO findShopById(long id) {
        ShopEntity shop = shopRepository.findById(id).orElseThrow(()
        -> new BadRequest("Not found shop with id = "+id));
        return ShopMapper.toShopDto(shop);
    }

    @Override
    public long countAllShop() {
        return shopRepository.count();
    }

    @Override
    public ShopDTO searchByName(String keyword) {
        List<ShopEntity> shops = shopRepository.findByNameContaining(keyword);
        List<ShopDTO> shopDTOS = new ArrayList<>();
        for(ShopEntity shop : shops){
            shopDTOS.add(ShopMapper.toShopDto(shop));
        }
        if(shopDTOS.isEmpty()){
            throw  new BadRequest("Not found shop name "+keyword);
        }
        return shopDTOS.get(0);
    }
}

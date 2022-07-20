package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.dto.AddressDTO;
import com.example.fsoft_shopee_nhom02.mapper.AddressMapper;
import com.example.fsoft_shopee_nhom02.model.AddressEntity;
import com.example.fsoft_shopee_nhom02.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public List<AddressDTO> getAllUserAddressByUserId(Long userId) {
        List<AddressEntity> userAddressEntityList = addressRepository.findByUserEntityId(userId);
        List<AddressDTO> userAddressDTOList = new ArrayList<>();
        for (AddressEntity address : userAddressEntityList) {
            userAddressDTOList.add(AddressMapper.toDto(address));
        }
        return userAddressDTOList;
    }
}

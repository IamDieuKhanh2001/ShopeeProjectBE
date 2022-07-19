package com.example.fsoft_shopee_nhom02.mapper;


import com.example.fsoft_shopee_nhom02.dto.AddressDTO;
import com.example.fsoft_shopee_nhom02.model.AddressEntity;

public class AddressMapper {

    public static AddressEntity toEntity (AddressDTO addressDTO){
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setAddress(addressDTO.getAddress());
        return addressEntity;
    }
}

package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.dto.AddressDTO;
import com.example.fsoft_shopee_nhom02.model.AddressEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.repository.AddressRepository;
import com.example.fsoft_shopee_nhom02.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    public List<AddressEntity> getAllUserAddressByUsername(String username) {
        List<AddressEntity> userAddressEntityList = addressRepository.findAddressEntitiesByUserEntityUsername(username);
        return userAddressEntityList;
    }

    public boolean saveUserAddress(AddressDTO addressDTO) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(addressDTO.getUsername());
        if(!userEntityOptional.isPresent()) {
            throw new IllegalStateException("Username not found!!");
        }
        AddressEntity newUserAddress = new AddressEntity(addressDTO.getAddress(), userEntityOptional.get());
        addressRepository.save(newUserAddress);
        return true;
    }

    public boolean deleteAddressByAddressId(Long addressId) {
        try {
            addressRepository.deleteById(addressId);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean updateAddress(Long addressId, String addressContent) {
        Optional<AddressEntity> addressEntityOptional = addressRepository.findById(addressId);
        if(!addressEntityOptional.isPresent()) {
            return false;
        }
        AddressEntity addressEntity = addressEntityOptional.get();
        addressEntity.setAddress(addressContent);
        try {
            addressRepository.save(addressEntity);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}

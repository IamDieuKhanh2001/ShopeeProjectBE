package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.auth.ApplicationUserService;
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

    public boolean saveUserAddress(AddressDTO addressDTO, String username) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
        if(!userEntityOptional.isPresent()) {
            throw new IllegalStateException("Username " + username + " not found");
        }
        AddressEntity newUserAddress = new AddressEntity(
                addressDTO.getAddress(),
                addressDTO.getName(),
                addressDTO.getPhone(),
                userEntityOptional.get());
        if(userEntityOptional.get().getAddressEntityList().size() == 0) {
            newUserAddress.setAddressDefault(true);
        }
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

    public boolean updateAddress(Long addressId, AddressDTO addressDTO) {
        Optional<AddressEntity> addressEntityOptional = addressRepository.findById(addressId);
        if(!addressEntityOptional.isPresent()) {
            return false;
        }
        AddressEntity addressEntity = addressEntityOptional.get();
        addressEntity.setAddress(addressDTO.getAddress());
        addressEntity.setName(addressDTO.getName());
        addressEntity.setPhoneNumber(addressDTO.getPhone());
        try {
            addressRepository.save(addressEntity);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean setDefaultAddress(Long addressId) {
        String usernameLogin = ApplicationUserService.GetUsernameLoggedIn();
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(usernameLogin);
        if(!userEntityOptional.isPresent()) {
            throw new IllegalStateException("Username " + usernameLogin + " not found");
        }
        AddressEntity currentDefaultAddress = addressRepository
                .findAddressEntityByAddressDefaultAndUserEntity(true, userEntityOptional.get());
        System.out.println(currentDefaultAddress.toString());
        currentDefaultAddress.setAddressDefault(false);
        AddressEntity newDefaultAddress = addressRepository.findById(addressId).get();
        newDefaultAddress.setAddressDefault(true);
        try {
            addressRepository.save(currentDefaultAddress);
            addressRepository.save(newDefaultAddress);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}

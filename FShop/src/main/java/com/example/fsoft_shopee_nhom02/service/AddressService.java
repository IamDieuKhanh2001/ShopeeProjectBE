package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.auth.ApplicationUserService;
import com.example.fsoft_shopee_nhom02.dto.AddressDTO;
import com.example.fsoft_shopee_nhom02.exception.BadRequest;
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
            throw new BadRequest("Username " + username + " not found");
        }
        AddressEntity newUserAddress = new AddressEntity(
                addressDTO.getAddress(),
                addressDTO.getName(),
                addressDTO.getPhone(),
                addressDTO.getWard(),
                addressDTO.getCity(),
                addressDTO.getDistrict(),
                userEntityOptional.get());
        if(userEntityOptional.get().getAddressEntityList().size() == 0) {
            newUserAddress.setAddressDefault(true);
        }
        addressRepository.save(newUserAddress);
        return true;
    }

    public boolean deleteAddressByAddressId(Long addressId) {
        Optional<AddressEntity> address = addressRepository.findById(addressId);
        if(!address.isPresent()) {
            return false;
        }
        if (address.get().getAddressDefault()){                     //Kiểm tra xem địa chỉ bị xóa có phải default không
            try {
                String username = address.get().getUserEntity().getUsername();
                addressRepository.deleteById(addressId);
                List<AddressEntity> listaddress = addressRepository.findAddressEntitiesByUserEntityUsername(username);  //Load lại danh sách địa chỉ sau khi bị xóa
                if (listaddress.size()>0) {
                    long id = listaddress.get(0).getId();
                    Optional<AddressEntity> newDefault = addressRepository.findById(id);        //Lấy địa chỉ đầu danh sách
                    newDefault.get().setAddressDefault(true);                                   //Gán thành địa chỉ Default
                    addressRepository.save(newDefault.get());
                }
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
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
        addressEntity.setCity(addressDTO.getCity());
        addressEntity.setDistrict(addressDTO.getDistrict());
        addressEntity.setWard(addressDTO.getWard());
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
        Optional<AddressEntity> addressOptional = addressRepository.findById(addressId);
        if(!addressOptional.isPresent()) {
            return false;
        }

        AddressEntity currentDefaultAddress = addressRepository
                .findAddressEntityByAddressDefaultAndUserEntity(true, userEntityOptional.get());
        currentDefaultAddress.setAddressDefault(false);

        AddressEntity newDefaultAddress = addressOptional.get();
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

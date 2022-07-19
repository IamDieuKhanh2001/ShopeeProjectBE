package com.example.fsoft_shopee_nhom02.auth;

import com.example.fsoft_shopee_nhom02.dto.AddressDTO;
import com.example.fsoft_shopee_nhom02.dto.UserDTO;
import com.example.fsoft_shopee_nhom02.mapper.AddressMapper;
import com.example.fsoft_shopee_nhom02.mapper.UserMapper;
import com.example.fsoft_shopee_nhom02.model.AddressEntity;
import com.example.fsoft_shopee_nhom02.model.CartEntity;
import com.example.fsoft_shopee_nhom02.model.RoleEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.repository.RoleRepository;
import com.example.fsoft_shopee_nhom02.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ApplicationUserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByUsername(username);

        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));

        return user.map(ApplicationUser::new).get();
    }

    public UserEntity save(UserDTO userDTO) {
        Optional<UserEntity> DAOUserOptional = userRepository.findByUsername(userDTO.getUsername());
        if(DAOUserOptional.isPresent()){
            throw new IllegalStateException("Username have been used! try another username"); //Username đã dc sd
        }
        if(userDTO.getPassword() == null || userDTO.getPassword().length() <= 6) {
            throw new IllegalStateException("Password must be longer than 6 character and can't be null");
        }

        //Nếu không trùng username, encode pwd và lưu vào db user
        Optional<RoleEntity> roleUserOptional = roleRepository.findById(Long.parseLong("2")); //Lấy ROLE_USER
        List<RoleEntity> roleUserList = new ArrayList<>();
        roleUserOptional.ifPresent(roleUserList::add);
        Set<RoleEntity> roleUserSet = new HashSet<>(roleUserList);//ép kiểu role thành set gán cho entity user
        UserEntity newUser = UserMapper.toEntity(userDTO); //Parse DTO sang Entity
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword())); //Bcrypt password tk
        newUser.setRoleEntitySet(roleUserSet);

        List<AddressDTO>  addressDtoList = userDTO.getAddressList(); //Xử lí lưu address
        List<AddressEntity> addressEntityList = new ArrayList<>();
        addressDtoList.forEach(item -> {
            AddressEntity addressEntity = new AddressEntity(item.getAddress(), newUser);
            addressEntityList.add(addressEntity);
        });
        newUser.setAddressEntityList(addressEntityList);

        newUser.setCartEntity(new CartEntity()); //tạo 1 cart cho user

        return userRepository.save(newUser);
    }
}

package com.example.fsoft_shopee_nhom02.auth;

import com.example.fsoft_shopee_nhom02.dto.UserDTO;
import com.example.fsoft_shopee_nhom02.mapper.UserMapper;
import com.example.fsoft_shopee_nhom02.model.CartEntity;
import com.example.fsoft_shopee_nhom02.model.RoleEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.repository.RoleRepository;
import com.example.fsoft_shopee_nhom02.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public static String GetUsernameLoggedIn() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof ApplicationUser) {
            username = ((ApplicationUser)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

    public Boolean save(UserDTO userDTO){
        Optional<UserEntity> DAOUsernameOptional = userRepository.findByUsername(userDTO.getUsername());
        if(DAOUsernameOptional.isPresent()){
            throw new IllegalStateException("Username have been used! Please try another username"); //Username đã dc sd
        }

        if(Objects.equals(userDTO.getEmail(), "")){
            throw new IllegalStateException("Email not null! Please try another Email"); //Username đã dc sd
        }
        Optional<UserEntity> DAOUserEmailOptional = userRepository.findByEmail(userDTO.getEmail());
        if(DAOUserEmailOptional.isPresent()){
            throw new IllegalStateException("Email have been registered for another account! Please try another Email"); //Username đã dc sd
        }

        if(userDTO.getPassword() == null || userDTO.getPassword().length() <= 6) {
            throw new IllegalStateException("Password must be longer than 7 character and can't be null");
        }

        //Nếu không trùng username, encode pwd và lưu vào db user
        Optional<RoleEntity> roleUserOptional = roleRepository.findById(Long.parseLong("2")); //Lấy ROLE_USER
        List<RoleEntity> roleUserList = new ArrayList<>();
        roleUserOptional.ifPresent(roleUserList::add);
        Set<RoleEntity> roleUserSet = new HashSet<>(roleUserList);//ép kiểu role thành set gán cho entity user

        UserEntity newUser = UserMapper.toEntity(userDTO); //Parse DTO sang Entity
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword())); //Bcrypt password tk
        newUser.setRoleEntitySet(roleUserSet);

        newUser.setCartEntity(new CartEntity()); //tạo 1 cart cho user

        userRepository.save(newUser);
        return true;
    }
}

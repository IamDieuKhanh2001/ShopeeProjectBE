package com.example.fsoft_shopee_nhom02.auth;

import com.example.fsoft_shopee_nhom02.config.GlobalVariable;
import com.example.fsoft_shopee_nhom02.dto.AddressDTO;
import com.example.fsoft_shopee_nhom02.dto.UserDTO;
import com.example.fsoft_shopee_nhom02.mapper.UserMapper;
import com.example.fsoft_shopee_nhom02.model.AddressEntity;
import com.example.fsoft_shopee_nhom02.model.CartEntity;
import com.example.fsoft_shopee_nhom02.model.RoleEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.repository.RoleRepository;
import com.example.fsoft_shopee_nhom02.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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


    public ResponseEntity<?> save(UserDTO userDTO) {
        Optional<UserEntity> DAOUsernameOptional = userRepository.findByUsername(userDTO.getUsername());
        if(userDTO.getPassword() == null || userDTO.getPassword().length() <= 6) {
            return new ResponseEntity<>("Password must be longer than 7 character and can't be null", HttpStatus.BAD_REQUEST);
        }

        //N???u kh??ng tr??ng username, encode pwd v?? l??u v??o db user
        Optional<RoleEntity> roleUserOptional = roleRepository.findById(Long.parseLong("2")); //L???y ROLE_USER
        List<RoleEntity> roleUserList = new ArrayList<>();
        roleUserOptional.ifPresent(roleUserList::add);
        Set<RoleEntity> roleUserSet = new HashSet<>(roleUserList);//??p ki???u role th??nh set g??n cho entity user

        UserEntity newUser = UserMapper.toEntity(userDTO); //Parse DTO sang Entity
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword())); //Bcrypt password tk
        newUser.setRoleEntitySet(roleUserSet);

        newUser.setCartEntity(new CartEntity()); //t???o 1 cart cho user

        newUser.setCreatedDate(new Timestamp(System.currentTimeMillis()));      //th???i gian ????ng k??
        newUser.setModifiedDate(new Timestamp(System.currentTimeMillis()));     //L??c t???o m???i th???i gian ????ng k?? b???ng th???i gian ch???nh s???a

        newUser.setName("Unname#" + GlobalVariable.GetOTP());
        newUser.setGender("3"); //Gender Kh??c
        userRepository.save(newUser);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}

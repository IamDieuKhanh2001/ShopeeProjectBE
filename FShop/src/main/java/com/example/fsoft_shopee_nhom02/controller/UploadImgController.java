package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.auth.ApplicationUserService;
import com.example.fsoft_shopee_nhom02.model.ProductEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.repository.ProductRepository;
import com.example.fsoft_shopee_nhom02.repository.UserRepository;
import com.example.fsoft_shopee_nhom02.service.CloudinaryService;
import com.example.fsoft_shopee_nhom02.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
public class UploadImgController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/users/avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("avatar") MultipartFile avatar){
        String username = ApplicationUserService.GetUsernameLoggedIn();
        UserEntity userLogin = userService.findByUsername(username);
        String url = cloudinaryService.uploadFile(
                avatar,
                String.valueOf(userLogin.getId()),
                "ShopeeProject" + "/" + "Avatar");
        userLogin.setAvatar(url);
        userRepository.save(userLogin);
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/admin/product-img/{id}")
    public ResponseEntity<?> uploadProductImg(@PathVariable long id,
                                              @RequestParam("imageProduct") MultipartFile imageProduct)
//                                              @RequestParam("image1") MultipartFile image1,
//                                              @RequestParam("image2") MultipartFile image2,
//                                              @RequestParam("image3") MultipartFile image3,
//                                              @RequestParam("image4") MultipartFile image4)
    {
        ProductEntity product = productRepository.findById(id).get();

        String imgPro = cloudinaryService.uploadFile(
                imageProduct,
                "ImageProduct",
                "ShopeeProject" + "/" + "Product" + "/" + product.getId());

        product.setImageProduct(imgPro);
        productRepository.save(product);

        return ResponseEntity.ok("kkk");
    }
}

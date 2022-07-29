package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.auth.ApplicationUserService;
import com.example.fsoft_shopee_nhom02.dto.SuccessResponseDTO;
import com.example.fsoft_shopee_nhom02.model.ProductEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.repository.ProductRepository;
import com.example.fsoft_shopee_nhom02.repository.UserRepository;
import com.example.fsoft_shopee_nhom02.service.CloudinaryService;
import com.example.fsoft_shopee_nhom02.service.ProductService;
import com.example.fsoft_shopee_nhom02.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private ProductService productService;

    @PostMapping("/users/avatar")
    public SuccessResponseDTO uploadAvatar(
            @RequestParam(value = "avatar", required = false) MultipartFile avatar
    ){
        if(!avatar.getContentType().equals("image/png") && !avatar.getContentType().equals("image/jpeg")) {
            throw new IllegalStateException("file khong hop le");
        }
        String username = ApplicationUserService.GetUsernameLoggedIn();
        if(userService.uploadUserAvatar(avatar, username)) {
            return new SuccessResponseDTO(
                    HttpStatus.OK,
                    "Update avatar user: " + username + " thanh cong");
        } else {
            throw new IllegalStateException("Update avatar user: " + username + " that bai");
        }
    }
//    image/png
//    image/jpeg

    @PostMapping("/admin/product-img/{id}")
    public ResponseEntity<?> uploadProductImg(@PathVariable long id,
                                              @RequestParam("imageProduct") MultipartFile imageProduct,
                                              @RequestParam(value = "image1", required = false) MultipartFile image1,
                                              @RequestParam(value = "image2", required = false) MultipartFile image2,
                                              @RequestParam(value = "image3", required = false) MultipartFile image3,
                                              @RequestParam(value = "image4", required = false) MultipartFile image4) {
        ProductEntity product = productService.getProductById(id);

        // ImageProduct url
        String imgProUrl = cloudinaryService.uploadFile(
                imageProduct,
                "ImageProduct",
                "ShopeeProject" + "/" + "Product" + "/" + product.getId());

        String imgOneUrl = cloudinaryService.uploadFile(
                image1,
                "Image1",
                "ShopeeProject" + "/" + "Product" + "/" + product.getId());

        String imgTwoUrl = cloudinaryService.uploadFile(
                image2,
                "Image2",
                "ShopeeProject" + "/" + "Product" + "/" + product.getId());

        String imgThreeUrl = cloudinaryService.uploadFile(
                image3,
                "Image3",
                "ShopeeProject" + "/" + "Product" + "/" + product.getId());

        String imgFourUrl = cloudinaryService.uploadFile(
                image4,
                "Image4",
                "ShopeeProject" + "/" + "Product" + "/" + product.getId());

        product.setImageProduct(imgProUrl);

        if(imgOneUrl != "-1")
            product.setImage1(imgOneUrl);

        if(imgTwoUrl != "-1")
            product.setImage2(imgTwoUrl);

        if(imgThreeUrl != "-1")
            product.setImage3(imgThreeUrl);

        if(imgFourUrl != "-1")
            product.setImage4(imgFourUrl);

        productService.saveProduct(product);

        return ResponseEntity.ok("Saved!");
    }
}

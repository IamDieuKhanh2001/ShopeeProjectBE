package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.auth.ApplicationUserService;
import com.example.fsoft_shopee_nhom02.dto.CommentDTO;
import com.example.fsoft_shopee_nhom02.dto.ProductDTO;
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

@RestController
public class UploadImgController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private UserService userService;

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
    @DeleteMapping("/users/avatar")
    public ResponseEntity<?> deleteAva(
            @RequestParam(value = "avatar", required = false) MultipartFile avatar
    ){

        return ResponseEntity.ok(
                cloudinaryService.deleteFile("ShopeeProject" + "/" + "Avatar" + "/" + "3")
        );
    }
//    image/png
//    image/jpeg

    @PostMapping("/admin/product-img/{id}")
    public ProductDTO uploadProductImg(@PathVariable long id,
                                       @RequestParam("imageProduct") MultipartFile imageProduct,
                                       @RequestParam(value = "image1", required = false) MultipartFile image1,
                                       @RequestParam(value = "image2", required = false) MultipartFile image2,
                                       @RequestParam(value = "image3", required = false) MultipartFile image3,
                                       @RequestParam(value = "image4", required = false) MultipartFile image4) {

        return productService.saveProductImage(id, imageProduct, image1, image2, image3, image4);

    }

    @PostMapping("/users/comments-img/{id}")
    public CommentDTO uploadCommentImg(@PathVariable long id,
                                       @RequestParam(required = false) MultipartFile img) {
        String username = ApplicationUserService.GetUsernameLoggedIn();

        if(!img.getContentType().equals("image/png") && !img.getContentType().equals("image/jpeg")) {
            throw new IllegalStateException("file khong hop le");
        }

        return productService.postCommentImg(id, img, username);
    }
}

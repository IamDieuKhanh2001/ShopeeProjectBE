package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.auth.ApplicationUserService;
import com.example.fsoft_shopee_nhom02.dto.*;
import com.example.fsoft_shopee_nhom02.model.ProductEntity;
import com.example.fsoft_shopee_nhom02.model.UserEntity;
import com.example.fsoft_shopee_nhom02.repository.ProductRepository;
import com.example.fsoft_shopee_nhom02.repository.UserRepository;
import com.example.fsoft_shopee_nhom02.service.*;
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
    @Autowired
    private SubCategoryService subCategoryService;
    @Autowired
    private ShopService shopService;

    @PostMapping("/users/avatar")
    public ResponseEntity<?> uploadAvatar(
            @RequestParam(value = "avatar", required = false) MultipartFile avatar
    ){
        if(!avatar.getContentType().equals("image/png") && !avatar.getContentType().equals("image/jpeg")) {
            return ResponseEntity.badRequest().body("file khong thuoc dinh dang jpeg, png");
        }
        String username = ApplicationUserService.GetUsernameLoggedIn();
        if(userService.uploadUserAvatar(avatar, username)) {
            return ResponseEntity.ok(new SuccessResponseDTO(
                    HttpStatus.OK,
                    "Update avatar user: " + username + " thanh cong"));
        } else {
            return ResponseEntity.badRequest().body("Update avatar user: " + username + " that bai");
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

    @PostMapping("/admin/products/{id}/image")
    public ProductDTO uploadProductImg(@PathVariable long id,
                                       @RequestParam("imageProduct") MultipartFile imageProduct,
                                       @RequestParam(value = "image1", required = false) MultipartFile image1,
                                       @RequestParam(value = "image2", required = false) MultipartFile image2,
                                       @RequestParam(value = "image3", required = false) MultipartFile image3,
                                       @RequestParam(value = "image4", required = false) MultipartFile image4) {

        return productService.saveProductImage(id, imageProduct, image1, image2, image3, image4);

    }

    @PostMapping("/users/comments/{id}/image")
    public ResponseEntity<?> uploadCommentImg(@PathVariable long id,
                                       @RequestParam(required = false) MultipartFile img) {
        String username = ApplicationUserService.GetUsernameLoggedIn();

        if(!img.getContentType().equals("image/png") && !img.getContentType().equals("image/jpeg")) {
            return new ResponseEntity<>("File khong hop le!", HttpStatus.BAD_REQUEST);
        }

        return productService.postCommentImg(id, img, username);
    }

    @PostMapping("/admin/subcategory/{id}/image")
    public SubCategoryDTO uploadSubcategoryImg(@PathVariable long id,
                                           @RequestParam MultipartFile image) {

        return subCategoryService.uploadImage(id,image);

    }

    @PostMapping("/admin/shop/{id}/image")
    public ShopDTO uploadShopImg(@PathVariable long id,
                                 @RequestParam MultipartFile avatar,
                                 @RequestParam MultipartFile background) {

        return shopService.uploadImage(id,avatar,background);
    }
}

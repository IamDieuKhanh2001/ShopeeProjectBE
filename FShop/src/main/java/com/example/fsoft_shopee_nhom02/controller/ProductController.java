package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.dto.CommentDTO;
import com.example.fsoft_shopee_nhom02.dto.ProductDTO;
import com.example.fsoft_shopee_nhom02.dto.ListOutputResult;
import com.example.fsoft_shopee_nhom02.model.ProductEntity;
import com.example.fsoft_shopee_nhom02.model.TypeEntity;
import com.example.fsoft_shopee_nhom02.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/admin/products")
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        return productService.save(productDTO);
    }

    // Sua cac truong cua bang Product
    @PutMapping("/admin/products/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable long id, @RequestBody ProductDTO product) {
        product.setId(id);
        return ResponseEntity.ok(productService.save(product));
    }

    // Xoa 1 product trong bang Product (Chua xet bang Type)
    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<ProductEntity> deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Lay products co phan trang
    @GetMapping("/products")
    public ListOutputResult getAllProducts(@RequestParam Map<String, String> requestParams) {
        String page = requestParams.get("page");
        String limit = requestParams.get("limit");

        return productService.getAllProducts(page, limit);
    }

    @GetMapping("/products/{id}")
    public ProductDTO getProductDetail(@PathVariable long id) {
        // Cập nhật số lượt view sản phẩm
        productService.updateViewProduct(id);
        // Trả về thông tin sản phẩm
        return productService.getDetail(id);
    }

    // Lay types cua san pham tuong ung
    @GetMapping("/products/{id}/types")
    public List<TypeEntity> getProductTypes(@PathVariable long id) {
        return productService.getTypes(id);
    }

    @PostMapping("/admin/products/{id}/types")
    public List<TypeEntity> createProductTypes(@PathVariable long id, @RequestBody List<TypeEntity> types) {
        return productService.createTypes(id, types);
    }

    @PutMapping("/admin/products/{id}/types")
    public List<?> updateProductTypes(@PathVariable long id, @RequestBody List<TypeEntity> types) {
        return productService.updateAllTypes(id, types);
    }

    @GetMapping("/products/search")
    public ListOutputResult searchProduct(@RequestParam Map<String, String> requestParams) {
        String page = requestParams.get("page");
        String limit = requestParams.get("limit");
        String keyword = requestParams.get("keyword");
        String minPrice = requestParams.get("min-price");
        String maxPrice = requestParams.get("max-price");
        String subCate = requestParams.get("sub");

        return productService.search(page, limit, keyword, minPrice, maxPrice, subCate);
    }

    @PostMapping("/users/products/{proId}/comments")
    CommentDTO postComment(@PathVariable long proId, @RequestBody CommentDTO commentDTO) {
        return productService.createComment(proId, commentDTO);
    }

    @GetMapping("/products/{id}/comments")
    ListOutputResult getComments(@PathVariable long id, @RequestParam Map<String, String> requestParams) {
        String page = requestParams.get("page");
        String limit = requestParams.get("limit");
        String rating = requestParams.get("rating");

        return productService.getAllComments(id, page, limit, rating);
    }
}

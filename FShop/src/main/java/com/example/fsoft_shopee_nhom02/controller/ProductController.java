package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.exception.ResourceNotFoundException;
import com.example.fsoft_shopee_nhom02.model.ProductEntity;
import com.example.fsoft_shopee_nhom02.repository.ProductRepository;
import com.example.fsoft_shopee_nhom02.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @PostMapping("")
    public ProductEntity createProduct(@RequestBody ProductEntity product) {
        return productRepository.save(product);
    }

    // Sua cac truong cua bang Product
    @PutMapping("/{id}")
    public ResponseEntity<ProductEntity> updateProduct(@PathVariable long id, @RequestBody ProductEntity product) {
        return ResponseEntity.ok(productService.updateById(id, product));
    }

    // Xoa 1 product trong bang Product (Chua xet bang Type)
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductEntity> deleteProduct(@PathVariable long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found " + id));

        productRepository.delete(product);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Lay products co phan trang
    @GetMapping("")
    public List<ProductEntity> getAllProducts(@RequestParam Map<String, String> requestParams) {
        String page = requestParams.get("page");
        String limit = requestParams.get("limit");

        return productService.getAllProducts(page, limit);
    }

    // Dem so products co trong Database
    @GetMapping("/all-items")
    public int countProducts() {
        return (int) productRepository.count();
    }
}

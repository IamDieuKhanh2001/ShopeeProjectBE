package com.example.fsoft_shopee_nhom02.controller;

import com.example.fsoft_shopee_nhom02.dto.ProductDTO;
import com.example.fsoft_shopee_nhom02.exception.ResourceNotFoundException;
import com.example.fsoft_shopee_nhom02.mapper.ProductMapper;
import com.example.fsoft_shopee_nhom02.model.ProductEntity;
import com.example.fsoft_shopee_nhom02.model.SubCategoryEntity;
import com.example.fsoft_shopee_nhom02.model.TypeEntity;
import com.example.fsoft_shopee_nhom02.repository.ProductRepository;
import com.example.fsoft_shopee_nhom02.repository.SubCategoryRepository;
import com.example.fsoft_shopee_nhom02.repository.TypeRepository;
import com.example.fsoft_shopee_nhom02.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Autowired
    TypeRepository typeRepository;

    @PostMapping("/admin")
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        return productService.save(productDTO);
    }

    // Sua cac truong cua bang Product
    @PutMapping("/admin/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable long id, @RequestBody ProductDTO product) {
        product.setId(id);
        return ResponseEntity.ok(productService.save(product));
    }

    // Xoa 1 product trong bang Product (Chua xet bang Type)
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ProductEntity> deleteProduct(@PathVariable long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found " + id));

        productRepository.delete(product);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Lay products co phan trang
    @GetMapping("")
    public List<ProductDTO> getAllProducts(@RequestParam Map<String, String> requestParams) {
        String page = requestParams.get("page");
        String limit = requestParams.get("limit");

        return productService.getAllProducts(page, limit);
    }

    // Dem so products co trong Database
    @GetMapping("/all-items")
    public int countProducts() {
        return (int) productRepository.count();
    }

    @GetMapping("/{id}")
    public ProductDTO getProductDetail(@PathVariable long id) {
        return productService.getDetail(id);
    }

    // Lay types cua san pham tuong ung
    @GetMapping("/{id}/types")
    public List<TypeEntity> getProductTypes(@PathVariable long id) {
        return typeRepository.findAllByProductEntityId(id);
    }

    @PostMapping("/{id}/types")
    public TypeEntity createProductTypes(@PathVariable long id, @RequestBody TypeEntity typeEntity) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found " + id));

        typeEntity.setProductEntity(productEntity);

        return typeRepository.save(typeEntity);
    }
}

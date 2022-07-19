
package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.dto.ProductDTO;
import com.example.fsoft_shopee_nhom02.exception.ResourceNotFoundException;
import com.example.fsoft_shopee_nhom02.mapper.ProductMapper;
import com.example.fsoft_shopee_nhom02.model.ProductEntity;
import com.example.fsoft_shopee_nhom02.model.SubCategoryEntity;
import com.example.fsoft_shopee_nhom02.model.TypeEntity;
import com.example.fsoft_shopee_nhom02.repository.ProductRepository;
import com.example.fsoft_shopee_nhom02.repository.SubCategoryRepository;
import com.example.fsoft_shopee_nhom02.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Autowired
    TypeRepository typeRepository;

    public ProductDTO save(ProductDTO productDTO) {
        ProductEntity product;

        if(productDTO.getId() != 0) {
            // UPDATE
            product = productRepository.findById(productDTO.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cannot found " + productDTO.getId()));
            ProductMapper.toProductEntity(product, productDTO);
        }
        else {
            // CREATE
            product = ProductMapper.toProductEntity(productDTO);
        }
        SubCategoryEntity subCategoryEntity = subCategoryRepository.findById(productDTO.getSubCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found " + productDTO.getSubCategoryId()));
        product.setSubCategoryEntity(subCategoryEntity);


        return ProductMapper.toProductDTO(product);
    }

    public List<ProductDTO> getAllProducts(String page, String limit) {
        page = (page == null) ? "1"  : page;
        limit = (limit == null) ? "12" : limit;

        List<ProductDTO> productDTOS = new ArrayList<>();

        Pageable pageable = PageRequest.of((Integer.valueOf(page) - 1), Integer.valueOf(limit));

        List<ProductEntity> products = productRepository.findAll(pageable).getContent();

        for (ProductEntity product : products) {
            productDTOS.add(ProductMapper.toProductDTO(product));
        }

        if(products.isEmpty()) {
            throw new ResourceNotFoundException("Empty!");
        }

        return productDTOS;
    }

    public ProductDTO getDetail(long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found " + id));

        ProductDTO productDTO = ProductMapper.toProductDTO(product);

        return productDTO;
    }
}

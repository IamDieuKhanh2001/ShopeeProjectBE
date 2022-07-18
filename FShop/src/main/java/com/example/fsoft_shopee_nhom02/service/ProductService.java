
package com.example.fsoft_shopee_nhom02.service;

import com.example.fsoft_shopee_nhom02.exception.ResourceNotFoundException;
import com.example.fsoft_shopee_nhom02.model.ProductEntity;
import com.example.fsoft_shopee_nhom02.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public ProductEntity updateById(long id, ProductEntity product) {
        ProductEntity updateProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found " + id));
        updateProduct.setName(product.getName());
        updateProduct.setDescription(product.getDescription());
        updateProduct.setDetail(product.getDetail());
        updateProduct.setImageProduct(product.getImageProduct());
        updateProduct.setImage1(product.getImage1());
        updateProduct.setImage2(product.getImage2());
        updateProduct.setImage3(product.getImage3());
        updateProduct.setImage4(product.getImage4());
        updateProduct.setSale(product.getSale());

        productRepository.save(updateProduct);

        return updateProduct;
    }

    public List<ProductEntity> getAllProducts(String page, String limit) {
        page = (page == null) ? "1"  : page;
        limit = (limit == null) ? "12" : limit;

        Pageable pageable = PageRequest.of((Integer.valueOf(page) - 1), Integer.valueOf(limit));

        List<ProductEntity> products = productRepository.findAll(pageable).getContent();

        if(products.isEmpty()) {
            throw new ResourceNotFoundException("Empty!");
        }

        return products;
    }

}

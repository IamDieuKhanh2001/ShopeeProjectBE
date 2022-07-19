package com.example.fsoft_shopee_nhom02.mapper;

import com.example.fsoft_shopee_nhom02.dto.ProductDTO;
import com.example.fsoft_shopee_nhom02.model.ProductEntity;

public class ProductMapper {
    public static ProductDTO toProductDTO(ProductEntity productEntity) {
        ProductDTO productDTO = new ProductDTO();

        productDTO.setId(productEntity.getId());
        productDTO.setName(productEntity.getName());
        productDTO.setDescription(productEntity.getDescription());
        productDTO.setDetail(productEntity.getDetail());
        productDTO.setImageProduct(productEntity.getImageProduct());
        productDTO.setImage1(productEntity.getImage1());
        productDTO.setImage2(productEntity.getImage2());
        productDTO.setImage3(productEntity.getImage3());
        productDTO.setImage4(productEntity.getImage4());
        productDTO.setSale(productEntity.getSale());
        productDTO.setSubCategoryId(productEntity.getSubCategoryId());

        return productDTO;
    }

    public static ProductEntity toProductEntity(ProductDTO productDTO) {
        ProductEntity productEntity = new ProductEntity();

        productEntity.setName(productDTO.getName());
        productEntity.setDescription(productDTO.getDescription());
        productEntity.setDetail(productDTO.getDetail());
        productEntity.setImageProduct(productDTO.getImageProduct());
        productEntity.setImage1(productDTO.getImage1());
        productEntity.setImage2(productDTO.getImage2());
        productEntity.setImage3(productDTO.getImage3());
        productEntity.setImage4(productDTO.getImage4());
        productEntity.setSale(productDTO.getSale());

        return productEntity;
    }

    public static ProductEntity toProductEntity(ProductEntity productEntity, ProductDTO productDTO) {
        productEntity.setName(productDTO.getName());
        productEntity.setDescription(productDTO.getDescription());
        productEntity.setDetail(productDTO.getDetail());
        productEntity.setImageProduct(productDTO.getImageProduct());
        productEntity.setImage1(productDTO.getImage1());
        productEntity.setImage2(productDTO.getImage2());
        productEntity.setImage3(productDTO.getImage3());
        productEntity.setImage4(productDTO.getImage4());
        productEntity.setSale(productDTO.getSale());

        return productEntity;
    }

}

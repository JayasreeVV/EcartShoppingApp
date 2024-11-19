package com.ecart.product_service.service;

import com.ecart.product_service.dto.ProductRequest;
import com.ecart.product_service.dto.ProductResponse;
import com.ecart.product_service.entity.Product;
import com.ecart.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = productMapper(productRequest);
        Product savedProduct = productRepository.save(product);
        return this.productResponseMapper(savedProduct);
    }

    private Product productMapper(ProductRequest productRequest) {
        return Product.builder()
                .price(productRequest.getPrice())
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .build();
    }

    private ProductResponse productResponseMapper(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .build();
    }

    public List<ProductResponse> getAllProducts() {
        return this.productRepository.findAll().stream().map(this::productResponseMapper).collect(Collectors.toList());
    }
}

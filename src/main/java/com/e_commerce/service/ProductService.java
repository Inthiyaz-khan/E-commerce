package com.e_commerce.service;

import com.e_commerce.dto.ProductDTO;
import com.e_commerce.dto.ProductResponse;
import com.e_commerce.model.Product;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, Product product);

    ProductResponse getAllProducts();

    ProductResponse getProductsByCategory(Long categoryId);

    ProductResponse getProductsByKeyword(String keyword);
}

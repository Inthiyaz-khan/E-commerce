package com.e_commerce.controller;

import com.e_commerce.dto.ProductDTO;
import com.e_commerce.dto.ProductResponse;
import com.e_commerce.model.Product;
import com.e_commerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/prod/add/{categoryId}")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody Product product, @PathVariable Long categoryId) {
        ProductDTO newProduct = productService.addProduct(categoryId, product);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @GetMapping("/public/prod/products")
    public ResponseEntity<ProductResponse> getAllProducts() {
        ProductResponse allProducts = productService.getAllProducts();
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @GetMapping("/public/prod/byCategory/{categoryId}")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId) {
        ProductResponse productsByCategory = productService.getProductsByCategory(categoryId);
        return new ResponseEntity<>(productsByCategory, HttpStatus.OK);
    }

    @GetMapping("/public/prod/byKeyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword) {
        ProductResponse productsByKeyword = productService.getProductsByKeyword(keyword);
        return new ResponseEntity<>(productsByKeyword, HttpStatus.FOUND);
    }
}

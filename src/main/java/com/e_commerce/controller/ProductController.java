package com.e_commerce.controller;

import com.e_commerce.dto.ProductDTO;
import com.e_commerce.dto.ProductResponse;
import com.e_commerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/prod/add/{categoryId}")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long categoryId) {
        ProductDTO newProduct = productService.addProduct(categoryId, productDTO);
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

    @PutMapping("/admin/prod/update/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long productId){
        ProductDTO updatedProduct = productService.updateProduct(productDTO, productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/admin/prod/delete/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) {
        ProductDTO deletedProduct = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }

    @PutMapping("/admin/prod/updateImg/{productId}")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, @RequestParam("image")MultipartFile image) throws IOException {
        ProductDTO updatedProduct = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}

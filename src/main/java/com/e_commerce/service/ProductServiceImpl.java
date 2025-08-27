package com.e_commerce.service;

import com.e_commerce.dto.ProductDTO;
import com.e_commerce.dto.ProductResponse;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.model.Category;
import com.e_commerce.model.Product;
import com.e_commerce.repository.CategoryRepository;
import com.e_commerce.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductResponse productResponse;

    @Override
    public ProductDTO addProduct(Long categoryId, Product product) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));
        product.setImage("Image");
        product.setCategory(category);
        product.setSpecialPrice(product.getPrice()-((product.getDiscount())*0.01) * product.getPrice());
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> allProducts = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        productResponse.setProductResponse(allProducts);
        return productResponse;
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        List<Product> productsByCategory = productRepository.findByCategory(category);
        List<ProductDTO> productDTOS = productsByCategory.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        productResponse.setProductResponse(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword) {
        List<Product> productsByKeyword = productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%');
        List<ProductDTO> productDTOS = productsByKeyword.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        productResponse.setProductResponse(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Product product, Long productId) {
        Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product","productId", productId));
        existingProduct.setProductName(product.getProductName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDiscount(product.getDiscount());
        existingProduct.setSpecialPrice(product.getPrice()-((product.getDiscount())*0.01) * product.getPrice());
        Product updatedProduct = productRepository.save(existingProduct);
        return modelMapper.map(updatedProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId",productId));
        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }
}

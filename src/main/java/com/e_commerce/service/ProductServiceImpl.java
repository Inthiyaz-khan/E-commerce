package com.e_commerce.service;

import com.e_commerce.dto.ProductDTO;
import com.e_commerce.dto.ProductResponse;
import com.e_commerce.exception.APIException;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.model.Category;
import com.e_commerce.model.Product;
import com.e_commerce.repository.CategoryRepository;
import com.e_commerce.repository.ProductRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

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

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(Long categoryId, @Valid ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        boolean isProductNotPresent = true;
        List<Product> products = category.getProducts();
        for (Product prod : products) {
            if (prod.getProductName().equals(productDTO.getProductName())) {
                isProductNotPresent = false;
                break;
            }
        }
        if (isProductNotPresent) {
            Product product = modelMapper.map(productDTO, Product.class);
            product.setImage("Image");
            product.setCategory(category);
            product.setSpecialPrice(product.getPrice()-((product.getDiscount())*0.01) * product.getPrice());
            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        }
        else
            throw new APIException("Product already present!");
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> allProducts = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        if (products.isEmpty())
            throw new APIException("No products exist!");
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
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product","productId", productId));
//        Product product = modelMapper.map(productDTO, Product.class);
        existingProduct.setProductName(productDTO.getProductName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setQuantity(productDTO.getQuantity());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setDiscount(productDTO.getDiscount());
        existingProduct.setSpecialPrice(productDTO.getPrice()-((productDTO.getDiscount())*0.01) * productDTO.getPrice());
        Product updatedProduct = productRepository.save(existingProduct);
        return modelMapper.map(updatedProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId",productId));
        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        String fileName = fileService.uploadImage(path, image);
        product.setImage(fileName);
        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }


}

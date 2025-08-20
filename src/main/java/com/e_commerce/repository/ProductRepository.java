package com.e_commerce.repository;

import com.e_commerce.model.Category;
import com.e_commerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    List<Product> findByProductNameLikeIgnoreCase(String keyword);
}

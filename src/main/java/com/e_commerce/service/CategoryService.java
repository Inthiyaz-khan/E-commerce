package com.e_commerce.service;

import com.e_commerce.dto.CategoryRequest;
import com.e_commerce.dto.CategoryResponse;

public interface CategoryService {
    CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    CategoryRequest createCategory(CategoryRequest categoryRequest);
    CategoryRequest deleteCategory(Long id);
    CategoryRequest updateCategory(CategoryRequest categoryRequest, Long id);
}

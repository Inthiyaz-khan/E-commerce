package com.e_commerce.service;

import com.e_commerce.dto.RequestCategory;
import com.e_commerce.dto.ResponseCategory;

public interface CategoryService {
    ResponseCategory getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    RequestCategory createCategory(RequestCategory requestCategory);
    RequestCategory deleteCategory(Long id);
    RequestCategory updateCategory(RequestCategory requestCategory, Long id);
}

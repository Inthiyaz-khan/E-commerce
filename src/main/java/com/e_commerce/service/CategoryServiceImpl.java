package com.e_commerce.service;

import com.e_commerce.dto.CategoryRequest;
import com.e_commerce.dto.CategoryResponse;
import com.e_commerce.exception.APIException;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.model.Category;
import com.e_commerce.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
//        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
//        List<Category> category = categoryRepo.findAll(pageable).getContent();
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> categoryPage = categoryRepo.findAll(pageable);
        List<Category> category = categoryPage.getContent();
        if (category.isEmpty())
            throw new APIException("Categories are empty..!");

        List<CategoryRequest> requestCategories = category.stream()
                .map(c -> modelMapper.map(c, CategoryRequest.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        categoryResponse.setFirstPage(categoryPage.isFirst());
        categoryResponse.setCategoryResponse(requestCategories);
        return categoryResponse;
    }

    @Override
    public CategoryRequest createCategory(CategoryRequest categoryRequest) {
        Category newCategory = modelMapper.map(categoryRequest, Category.class);
        Category category = categoryRepo.findByCategoryName(newCategory.getCategoryName());
        if(category != null)
            throw new APIException("Category with name \""+ categoryRequest.getCategoryName()+"\" already exists..!");
        Category savedCategory = categoryRepo.save(newCategory);
        return modelMapper.map(savedCategory, CategoryRequest.class);
    }

    @Override
    public CategoryRequest deleteCategory(Long id) {
//        Category categoryModel = categoryRepo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        Category existingCategory = categoryRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId",id));
        categoryRepo.delete(existingCategory);
//        return "Category "+id+" deleted successfully";
        return modelMapper.map(existingCategory, CategoryRequest.class);
    }

    @Override
    public CategoryRequest updateCategory(CategoryRequest categoryRequest, Long id) {
//        Category newCategory = categoryRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        Category newCategory = categoryRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId",id));
        newCategory.setCategoryName(categoryRequest.getCategoryName());
        Category updatedCategory = categoryRepo.save(newCategory);
        return modelMapper.map(updatedCategory, CategoryRequest.class);
    }
}

package com.e_commerce.service;

import com.e_commerce.dto.RequestCategory;
import com.e_commerce.dto.ResponseCategory;
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
    public ResponseCategory getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
//        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
//        List<Category> category = categoryRepo.findAll(pageable).getContent();
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> categoryPage = categoryRepo.findAll(pageable);
        List<Category> category = categoryPage.getContent();
        if (category.isEmpty())
            throw new APIException("Categories are empty..!");

        List<RequestCategory> requestCategories = category.stream()
                .map(c -> modelMapper.map(c, RequestCategory.class))
                .toList();
        ResponseCategory responseCategory = new ResponseCategory();
        responseCategory.setPageNumber(categoryPage.getNumber());
        responseCategory.setPageSize(categoryPage.getSize());
        responseCategory.setTotalElements(categoryPage.getTotalElements());
        responseCategory.setTotalPages(categoryPage.getTotalPages());
        responseCategory.setLastPage(categoryPage.isLast());
        responseCategory.setFirstPage(categoryPage.isFirst());
        responseCategory.setCategoryResponse(requestCategories);
        return responseCategory;
    }

    @Override
    public RequestCategory createCategory(RequestCategory requestCategory) {
        Category newCategory = modelMapper.map(requestCategory, Category.class);
        Category category = categoryRepo.findByCategoryName(newCategory.getCategoryName());
        if(category != null)
            throw new APIException("Category with name \""+ requestCategory.getCategoryName()+"\" already exists..!");
        Category savedCategory = categoryRepo.save(newCategory);
        return modelMapper.map(savedCategory, RequestCategory.class);
    }

    @Override
    public RequestCategory deleteCategory(Long id) {
//        Category categoryModel = categoryRepo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        Category existingCategory = categoryRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId",id));
        categoryRepo.delete(existingCategory);
//        return "Category "+id+" deleted successfully";
        return modelMapper.map(existingCategory, RequestCategory.class);
    }

    @Override
    public RequestCategory updateCategory(RequestCategory requestCategory, Long id) {
//        Category newCategory = categoryRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        Category newCategory = categoryRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId",id));
        newCategory.setCategoryName(requestCategory.getCategoryName());
        Category updatedCategory = categoryRepo.save(newCategory);
        return modelMapper.map(updatedCategory, RequestCategory.class);
    }
}

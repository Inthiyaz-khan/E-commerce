package com.e_commerce.service;

import com.e_commerce.dto.CategoryDTO;
import com.e_commerce.dto.CategoryResponse;
import com.e_commerce.exception.APIException;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.model.CategoryModel;
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
//        List<CategoryModel> category = categoryRepo.findAll(pageable).getContent();
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<CategoryModel> categoryPage = categoryRepo.findAll(pageable);
        List<CategoryModel> category = categoryPage.getContent();
        if (category.isEmpty())
            throw new APIException("Categories are empty..!");

        List<CategoryDTO> categoryDTOS = category.stream()
                .map(c -> modelMapper.map(c, CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        categoryResponse.setFirstPage(categoryPage.isFirst());
        categoryResponse.setCategoryResponse(categoryDTOS);
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        CategoryModel categoryModel = modelMapper.map(categoryDTO, CategoryModel.class);
        CategoryModel category = categoryRepo.findByCategoryName(categoryModel.getCategoryName());
        if(category != null)
            throw new APIException("Category with name \""+categoryDTO.getCategoryName()+"\" already exists..!");
        CategoryModel savedCategory = categoryRepo.save(categoryModel);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long id) {
//        CategoryModel categoryModel = categoryRepo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        CategoryModel existingCategory = categoryRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId",id));
        categoryRepo.delete(existingCategory);
//        return "Category "+id+" deleted successfully";
        return modelMapper.map(existingCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long id) {
//        CategoryModel newCategory = categoryRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        CategoryModel newCategory = categoryRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId",id));
        newCategory.setCategoryName(categoryDTO.getCategoryName());
        CategoryModel updatedCategory = categoryRepo.save(newCategory);
        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }
}

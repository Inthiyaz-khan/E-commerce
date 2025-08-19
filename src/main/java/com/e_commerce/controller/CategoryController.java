package com.e_commerce.controller;

import com.e_commerce.config.AppConstants;
import com.e_commerce.dto.RequestCategory;
import com.e_commerce.dto.ResponseCategory;
import com.e_commerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category/api")
public class CategoryController {

    //    @Autowired //This also works in injecting the categoryService bean without using constructor injection.
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //@RequestMapping(value = "/public/categories",method = RequestMethod.GET)
    @GetMapping("/public/categories")
    public ResponseEntity<ResponseCategory> getCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
        ResponseCategory categories = categoryService.getCategories(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/admin/createCategory")
    public ResponseEntity<RequestCategory> createCategory(@Valid @RequestBody RequestCategory requestCategory) {
        RequestCategory newCategory = categoryService.createCategory(requestCategory);
        return new ResponseEntity<>(newCategory, HttpStatus.OK);
    }

    @DeleteMapping("/admin/deleteCategory/{id}")
    public ResponseEntity<RequestCategory> deleteCategory(@PathVariable Long id) {
        RequestCategory deletedCategory = categoryService.deleteCategory(id);
        return new ResponseEntity<>(deletedCategory, HttpStatus.OK);
        //return ResponseEntity.ok(status);
        //return ResponseEntity.status(HttpStatus.OK).body(status);
    }

    @PutMapping("/admin/updateCategory/{id}")
    public ResponseEntity<RequestCategory> updateCategory(@RequestBody RequestCategory requestCategory, @PathVariable Long id) {
        RequestCategory updatedCategory = categoryService.updateCategory(requestCategory, id);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }
}

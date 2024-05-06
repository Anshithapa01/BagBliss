package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.repository.CategoryRepository;
import com.ecommerce.library.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    @Override
    public Category save(Category category) {
//            Category categorySave = new Category(category.getName());
//            return categoryRepository.save(categorySave);
        String categoryName = category.getName();

        // Check if a category with the same name (case sensitive) already exists
        Category existingCategory = categoryRepository.findByNameIgnoreCase(categoryName);
        if (existingCategory != null) {
            throw new DataIntegrityViolationException("Category with name " + categoryName + " already exists.");
        }

        // No duplicate found, proceed with saving the category
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) {
        Category categoryUpdate = categoryRepository.findById(category.getId()).get();
        categoryUpdate.setName(category.getName());
        categoryUpdate.setActivated(category.isActivated());
        categoryUpdate.setDeleted(category.isDeleted());
        return categoryRepository.save(categoryUpdate);
    }

    @Override
    public List<Category> findAllByActivatedTrue() {
        return categoryRepository.findAllByActivatedTrue();
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        Category category = categoryRepository.getById(id);
        category.setActivated(false);
        category.setDeleted(true);
        categoryRepository.save(category);
    }

    @Override
    public void enableById(Long id) {
        Category category = categoryRepository.getById(id);
        category.setActivated(true);
        category.setDeleted(false);
        categoryRepository.save(category);
    }

    @Override
    public List<CategoryDto> getCategoriesAndSize() {
        List<CategoryDto> categories = categoryRepository.getCategoriesAndSize();
        return categories;
    }

}



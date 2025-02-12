package org.wildcodeschool.myblog.service;

import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.CategoryDTO;
import org.wildcodeschool.myblog.mapper.CategoryMapper;
import org.wildcodeschool.myblog.model.Category;
import org.wildcodeschool.myblog.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(
            CategoryRepository categoryRepository,
            CategoryMapper categoryMapper
    ) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryDTO> getAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(categoryMapper::convertToDTO).toList();
    }

    public CategoryDTO getById(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return null;
        }
        return categoryMapper.convertToDTO(category);
    }

    public CategoryDTO create(Category category) {
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.convertToDTO(savedCategory);
    }

    public CategoryDTO update(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return null;
        }
        category.setName(categoryDetails.getName());
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.convertToDTO(updatedCategory);
    }

    public boolean delete(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return false;
        }
        categoryRepository.delete(category);
        return true;
    }
}

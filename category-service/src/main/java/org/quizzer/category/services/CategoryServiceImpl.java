package org.quizzer.category.services;

import org.quizzer.category.dto.base.CategoryDto;
import org.quizzer.category.dto.creation.CategoryCreationDto;
import org.quizzer.category.dto.update.CategoryUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class CategoryServiceImpl implements CategoryService {
    @Override
    public Page<CategoryDto> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public CategoryDto create(CategoryCreationDto.Request request) {
        return null;
    }

    @Override
    public CategoryDto update(CategoryUpdateDto.Request request) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}

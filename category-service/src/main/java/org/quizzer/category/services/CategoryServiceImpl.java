package org.quizzer.category.services;

import org.quizzer.category.dto.base.CategoryDto;
import org.quizzer.category.dto.creation.CategoryCreationDto;
import org.quizzer.category.dto.page.PageDto;
import org.quizzer.category.dto.update.CategoryUpdateDto;
import org.quizzer.category.exceptions.CategoryNotFoundException;
import org.quizzer.category.exceptions.NameAlreadyExistsException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Override
    @Transactional(readOnly = true)
    public PageDto<CategoryDto> getAll(Pageable pageable) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto get(Long id) throws CategoryNotFoundException {
        return null;
    }

    @Override
    @Transactional
    public CategoryDto create(CategoryCreationDto.Request request) throws NameAlreadyExistsException {
        return null;
    }

    @Override
    @Transactional
    public CategoryDto update(Long id, CategoryUpdateDto.Request request) throws NameAlreadyExistsException, CategoryNotFoundException {
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) throws CategoryNotFoundException {

    }
}

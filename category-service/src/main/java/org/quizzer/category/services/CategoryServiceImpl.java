package org.quizzer.category.services;

import lombok.RequiredArgsConstructor;
import org.quizzer.category.domain.Category;
import org.quizzer.category.dto.base.CategoryDto;
import org.quizzer.category.dto.base.CategoryDtoMapper;
import org.quizzer.category.dto.creation.CategoryCreationDto;
import org.quizzer.category.dto.creation.CategoryCreationEntityMapper;
import org.quizzer.category.dto.page.PageDto;
import org.quizzer.category.dto.page.PageDtoMapper;
import org.quizzer.category.dto.update.CategoryUpdateDto;
import org.quizzer.category.dto.update.CategoryUpdateEntityMapper;
import org.quizzer.category.exceptions.CategoryNotFoundException;
import org.quizzer.category.exceptions.NameAlreadyExistsException;
import org.quizzer.category.repositories.CategoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryDtoMapper categoryDtoMapper;
    private final CategoryCreationEntityMapper.Request categoryCreationRequestEntityMapper;
    private final CategoryUpdateEntityMapper.Request categoryUpdateRequestEntityMapper;
    private final PageDtoMapper<CategoryDto> pagedCategoryDtoMapper;

    @Override
    @Transactional(readOnly = true)
    public PageDto<CategoryDto> getAll(Pageable pageable) {
        return pagedCategoryDtoMapper.toDto(
            categoryRepository
                .findAll(pageable)
                .map(categoryDtoMapper::toDto)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto get(Long id) throws CategoryNotFoundException {
        return categoryDtoMapper.toDto(
            categoryRepository
                .findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id=" + id + " doesn't exist"))
        );
    }

    @Override
    @Transactional
    public CategoryDto create(CategoryCreationDto.Request request) throws NameAlreadyExistsException {
        String name = request.getName();
        if (categoryRepository.existsByName(name)) {
            throw new NameAlreadyExistsException("Given name=" + name + " already exists");
        }

        Category category = categoryCreationRequestEntityMapper.toEntity(request);
        category = categoryRepository.save(category);
        return categoryDtoMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryDto update(Long id, CategoryUpdateDto.Request request) throws NameAlreadyExistsException, CategoryNotFoundException {
        Category category = categoryRepository
            .findById(id)
            .orElseThrow(() -> new CategoryNotFoundException("Category with id=" + id + " doesn't exist"));

        String name = request.getName();
        if (!category.getName().equals(name) && categoryRepository.findCategoryByName(name).isPresent()) {
            throw new NameAlreadyExistsException("Name " + name + " already exists");
        }

        category = categoryUpdateRequestEntityMapper.toEntity(Pair.of(category, request));
        categoryRepository.save(category);
        return categoryDtoMapper.toDto(category);
    }

    @Override
    @Transactional
    public void delete(Long id) throws CategoryNotFoundException {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException("Category with id=" + id + " doesn't exist");
        }

        categoryRepository.deleteById(id);
    }
}

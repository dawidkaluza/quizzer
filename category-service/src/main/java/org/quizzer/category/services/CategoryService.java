package org.quizzer.category.services;

import org.quizzer.category.dto.base.CategoryDto;
import org.quizzer.category.dto.creation.CategoryCreationDto;
import org.quizzer.category.dto.page.PageDto;
import org.quizzer.category.dto.update.CategoryUpdateDto;
import org.quizzer.category.exceptions.CategoryNotFoundException;
import org.quizzer.category.exceptions.NameAlreadyExistsException;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    /**
     * Return all categories that match pageable param
     * @param pageable specifies pagination details
     * @return page of categories
     */
    PageDto<CategoryDto> getAll(Pageable pageable);

    /**
     * Return a category with given id
     * @param id category id
     * @return category
     * @throws CategoryNotFoundException if category with given id doesn't exist
     */
    CategoryDto get(Long id) throws CategoryNotFoundException;

    /**
     * Create a new category
     * @param request creation request
     * @return created category object
     * @throws NameAlreadyExistsException if request's name already exists
     */
    CategoryDto create(CategoryCreationDto.Request request) throws NameAlreadyExistsException;

    /**
     * Update an existing category
     * @param id category id
     * @param request update request
     * @return created category object
     * @throws NameAlreadyExistsException if request's name already exists
     * @throws CategoryNotFoundException if category with given id doesn't exist
     */
    CategoryDto update(Long id, CategoryUpdateDto.Request request) throws NameAlreadyExistsException, CategoryNotFoundException;

    /**
     * Delete a category
     * @param id category id
     * @throws CategoryNotFoundException if category with given id doesn't exist
     */
    void delete(Long id) throws CategoryNotFoundException;
}

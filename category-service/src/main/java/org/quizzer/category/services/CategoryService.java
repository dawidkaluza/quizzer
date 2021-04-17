package org.quizzer.category.services;

import org.quizzer.category.dto.base.CategoryDto;
import org.quizzer.category.dto.creation.CategoryCreationDto;
import org.quizzer.category.dto.page.PageDto;
import org.quizzer.category.dto.update.CategoryUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    /**
     * Returns all categories which match pageable param
     * @param pageable specifies pagination details
     * @return page of categories
     */
    PageDto<CategoryDto> getAll(Pageable pageable);

    /**
     * Returns category by given id
     * @param id category id
     * @return category
     */
    CategoryDto get(Long id);

    /**
     * Creates new category
     * @param request category request
     * @return created category object
     */
    CategoryDto create(CategoryCreationDto.Request request);

    /**
     * Updates existing category
     * @param request category request
     * @return created category object
     */
    CategoryDto update(CategoryUpdateDto.Request request);

    /**
     * Deletes category
     * @param id category id
     */
    void delete(Long id);
}

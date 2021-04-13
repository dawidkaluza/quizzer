package org.quizzer.category.dto.base;

import org.quizzer.category.domain.Category;
import org.quizzer.category.dto.DtoMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CategoryDtoMapper implements DtoMapper<Category, CategoryDto> {
    @Override
    public CategoryDto toDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        BeanUtils.copyProperties(category, categoryDto);
        return categoryDto;
    }
}

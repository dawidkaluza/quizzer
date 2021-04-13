package org.quizzer.category.dto.creation;

import org.quizzer.category.domain.Category;
import org.quizzer.category.dto.EntityMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

public class CategoryCreationEntityMapper {
    @Component
    public static class Request implements EntityMapper<CategoryCreationDto.Request, Category> {
        @Override
        public Category toEntity(CategoryCreationDto.Request request) {
            Category category = new Category();
            BeanUtils.copyProperties(request, category);
            return category;
        }
    }
}

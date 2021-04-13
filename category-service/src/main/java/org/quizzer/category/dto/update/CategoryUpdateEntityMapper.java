package org.quizzer.category.dto.update;

import org.quizzer.category.domain.Category;
import org.quizzer.category.dto.EntityMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

public class CategoryUpdateEntityMapper {
    @Component
    public static class Request implements EntityMapper<CategoryUpdateDto.Request, Category> {
        @Override
        public Category toEntity(CategoryUpdateDto.Request request) {
            Category category = new Category();
            BeanUtils.copyProperties(request, category);
            return category;
        }
    }
}

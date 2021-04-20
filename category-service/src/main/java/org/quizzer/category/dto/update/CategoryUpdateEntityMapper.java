package org.quizzer.category.dto.update;

import org.quizzer.category.domain.Category;
import org.quizzer.category.dto.EntityMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

public class CategoryUpdateEntityMapper {
    @Component
    public static class Request implements EntityMapper<Pair<Category, CategoryUpdateDto.Request>, Category> {
        @Override
        public Category toEntity(Pair<Category, CategoryUpdateDto.Request> pair) {
            Category category = pair.getFirst();
            BeanUtils.copyProperties(pair.getSecond(), category);
            return category;
        }
    }
}

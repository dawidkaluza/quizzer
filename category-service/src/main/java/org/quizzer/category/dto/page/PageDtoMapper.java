package org.quizzer.category.dto.page;

import org.quizzer.category.dto.DtoMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageDtoMapper<T> implements DtoMapper<Page<T>, PageDto<T>> {
    @Override
    public PageDto<T> toDto(Page<T> page) {
        return new PageDto<T>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}

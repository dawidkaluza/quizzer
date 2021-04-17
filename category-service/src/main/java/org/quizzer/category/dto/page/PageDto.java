package org.quizzer.category.dto.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> {
    private List<T> content;

    private int number;

    private int size;

    private long totalElements;

    private int totalPages;
}

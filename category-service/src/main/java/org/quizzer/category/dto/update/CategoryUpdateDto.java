package org.quizzer.category.dto.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CategoryUpdateDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank
        @Size(min = 3, max = 192)
        private String name;

        @NotBlank
        @Size(min = 3, max = 512)
        private String description;
    }
}

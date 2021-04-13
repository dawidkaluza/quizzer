package org.quizzer.category.dto;

@FunctionalInterface
public interface DtoMapper<I, O> {
    O toDto(I object);
}

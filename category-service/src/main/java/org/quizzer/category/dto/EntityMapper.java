package org.quizzer.category.dto;

@FunctionalInterface
public interface EntityMapper<I, O> {
    O toEntity(I object);
}

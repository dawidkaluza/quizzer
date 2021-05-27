package org.quizzer.question.dto;

@FunctionalInterface
public interface EntityMapper<I, O> {
    O toEntity(I object);
}

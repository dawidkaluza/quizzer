package org.quizzer.question.dto;

@FunctionalInterface
public interface DtoMapper<I, O> {
    O toDto(I object);
}
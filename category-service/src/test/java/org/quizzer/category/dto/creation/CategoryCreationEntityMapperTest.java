package org.quizzer.category.dto.creation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.quizzer.category.domain.Category;
import org.quizzer.category.domain.CategoryStub;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryCreationEntityMapperTest {
    private CategoryCreationEntityMapper.Request entityMapper;

    @BeforeEach
    public void beforeEach() {
        entityMapper = new CategoryCreationEntityMapper.Request();
    }

    @ParameterizedTest
    @MethodSource("provideTestArgs")
    public void toEntity_someRequest_expectedEntity(CategoryCreationDto.Request providedDto, Category expectedCategory) {
        // Given, when
        Category producedCategory = entityMapper.toEntity(providedDto);

        // Then
        assertEquals(expectedCategory, producedCategory);
        assertNull(producedCategory.getId());
        assertEquals(expectedCategory.getName(), producedCategory.getName());
        assertEquals(expectedCategory.getDescription(), producedCategory.getDescription());
    }

    private static Stream<Arguments> provideTestArgs() {
        return Stream.of(
            Arguments.of(
                new CategoryCreationDto.Request("Michael", "Jordan"),
                CategoryStub.of(null, "Michael", "Jordan")
            ),
            Arguments.of(
                new CategoryCreationDto.Request("David", "Beckham"),
                CategoryStub.of(null, "David", "Beckham")
            ),
            Arguments.of(
                new CategoryCreationDto.Request("De", "Ba"),
                CategoryStub.of(null, "De", "Ba")
            )
        );
    }
}
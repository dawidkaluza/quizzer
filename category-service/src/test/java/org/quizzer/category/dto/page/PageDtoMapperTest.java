package org.quizzer.category.dto.page;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PageDtoMapperTest {
    @ParameterizedTest
    @MethodSource("provideTestArgs")
    public <T> void toDto_page_returnExpectedDto(Page<T> providedPage, PageDto<T> expectedDto) {
        // Given
        PageDtoMapper<T> mapper = new PageDtoMapper<>();

        // When
        PageDto<T> producedDto = mapper.toDto(providedPage);

        // Then
        assertEquals(expectedDto, producedDto);
    }

    private static Stream<Arguments> provideTestArgs() {
        return Stream.of(
            Arguments.of(
                createPage(List.of("First", "Second", "Third"), 0, 3, 9),
                new PageDto<>(List.of("First", "Second", "Third"), 0, 3, 9, 3)
            ),
            Arguments.of(
                createPage(List.of("Football", "Basketball", "Volleyball"), 2, 3, 9),
                new PageDto<>(List.of("Football", "Basketball", "Volleyball"), 2, 3, 9, 3)
            )
        );
    }

    private static <T> Page<T> createPage(List<T> content, int page, int size, int total) {
        return new PageImpl<>(
            content, PageRequest.of(page, size), total
        );
    }
}
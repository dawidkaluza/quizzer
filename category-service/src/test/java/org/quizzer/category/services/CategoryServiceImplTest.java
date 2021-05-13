package org.quizzer.category.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quizzer.category.domain.Category;
import org.quizzer.category.dto.base.CategoryDto;
import org.quizzer.category.dto.base.CategoryDtoMapper;
import org.quizzer.category.dto.creation.CategoryCreationDto;
import org.quizzer.category.dto.creation.CategoryCreationEntityMapper;
import org.quizzer.category.dto.page.PageDto;
import org.quizzer.category.dto.page.PageDtoMapper;
import org.quizzer.category.dto.update.CategoryUpdateDto;
import org.quizzer.category.dto.update.CategoryUpdateEntityMapper;
import org.quizzer.category.exceptions.CategoryNotFoundException;
import org.quizzer.category.exceptions.NameAlreadyExistsException;
import org.quizzer.category.repositories.CategoryRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.quizzer.category.domain.CategoryStub.*;

class CategoryServiceImplTest {
    private CategoryRepository categoryRepository;
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        categoryService = new CategoryServiceImpl(
            categoryRepository, new CategoryDtoMapper(),
            new CategoryCreationEntityMapper.Request(), new CategoryUpdateEntityMapper.Request(),
            new PageDtoMapper<>()
        );
    }

    @Test
    public void getAll_firstPageWithTenResults_returnPagedResults() {
        //Given
        mockFindAll(categoryRepository, 15);

        //When
        PageDto<CategoryDto> page = categoryService.getAll(PageRequest.of(0, 10));

        //Then
        assertEquals(page.getNumber(), 0);
        assertEquals(page.getSize(), 10);
        assertEquals(page.getTotalElements(), 15);
        assertEquals(page.getTotalPages(), 2);
        List<CategoryDto> content = page.getContent();
        for (int i = 1; i <= 10; i++) {
            CategoryDto category = content.get(i - 1);
            assertEquals(category.getId(), i);
            assertEquals(category.getName(), "#" + i + " name");
            assertEquals(category.getDescription(), "#" + i + " category description");
        }
    }

    @Test
    public void getAll_secondPageWithFiveResults_returnPagedResults() {
        //Given
        mockFindAll(categoryRepository, 12);

        //When
        PageDto<CategoryDto> page = categoryService.getAll(PageRequest.of(1, 5));

        //Then
        assertEquals(page.getNumber(), 1);
        assertEquals(page.getSize(), 5);
        assertEquals(page.getTotalElements(), 12);
        assertEquals(page.getTotalPages(), 3);
        List<CategoryDto> content = page.getContent();
        for (int i = 0; i < 5; i++) {
            CategoryDto category = content.get(i);
            int categoryId = i + 6;
            assertEquals(category.getId(), categoryId);
            assertEquals(category.getName(), "#" + categoryId + " name");
            assertEquals(category.getDescription(), "#" + categoryId + " category description");
        }
    }

    @Test
    public void get_nonExistingId_throwException() {
        //Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        //When, then
        assertThrows(CategoryNotFoundException.class, () -> categoryService.get(1L));
    }

    @Test
    public void get_existingId_returnCategoryDto() {
        //Given
        Optional<Category> optionalCategory = Optional.of(of(1L, "First category", "First category desc"));
        when(categoryRepository.findById(1L)).thenReturn(optionalCategory);

        //When
        CategoryDto category = categoryService.get(1L);

        //Then
        assertEquals(category.getId(), 1L);
        assertEquals(category.getName(), "First category");
        assertEquals(category.getDescription(), "First category desc");
    }

    @Test
    public void create_requestWithExistingName_throwException() {
        //Given
        when(categoryRepository.existsByName("First category")).thenReturn(true);

        //When, then
        assertThrows(
            NameAlreadyExistsException.class,
            () -> categoryService.create(
                new CategoryCreationDto.Request("First category", "First category description")
            )
        );
    }

    @Test
    public void create_nonExistingName_returnCreatedCategory() {
        //Given
        when(categoryRepository.existsByName("First category")).thenReturn(false);
        mockSave(categoryRepository);

        //When
        CategoryDto category = categoryService.create(new CategoryCreationDto.Request("First category", "First category description"));

        //Then
        assertNotNull(category.getId());
        assertEquals(category.getName(), "First category");
        assertEquals(category.getDescription(), "First category description");
    }

    @Test
    public void update_nonExistingCategory_throwException() {
        //Given
        when(categoryRepository.findById(any())).thenThrow(new CategoryNotFoundException("Category not found"));

        //When, then
        assertThrows(
            CategoryNotFoundException.class,
            () -> categoryService.update(69L, new CategoryUpdateDto.Request("New name", "New description"))
        );
    }

    @Test
    public void update_existingNameInDifferentCategory_throwException() {
        //Given
        Optional<Category> optionalFirstCategory = Optional.of(of(1L, "First category", "First category description"));
        when(categoryRepository.findById(1L)).thenReturn(optionalFirstCategory);
        Optional<Category> optionalSecondCategory = Optional.of(of(2L, "Second category", "Second category description"));
        when(categoryRepository.findCategoryByName("Second category")).thenReturn(optionalSecondCategory);

        //When, then
        assertThrows(
            NameAlreadyExistsException.class,
            () -> categoryService.update(
                1L, new CategoryUpdateDto.Request("Second category", "New first category description")
            )
        );
    }

    @Test
    public void update_existingNameInSameCategory_returnUpdatedCategory() {
        //Given
        Optional<Category> optionalCategory = Optional.of(of(2L, "Second category", "Second category description"));
        when(categoryRepository.findById(2L)).thenReturn(optionalCategory);
        when(categoryRepository.findCategoryByName("Second category")).thenReturn(optionalCategory);

        //When
        CategoryDto categoryDto = categoryService.update(
            2L, new CategoryUpdateDto.Request("Second category", "New second category description")
        );

        //Then
        assertEquals(categoryDto.getId(), 2L);
        assertEquals(categoryDto.getName(), "Second category");
        assertEquals(categoryDto.getDescription(), "New second category description");
    }

    @Test
    public void update_nonExistingName_returnUpdatedCategory() {
        //Given
        Optional<Category> optionalCategory = Optional.of(of(2L, "Second category", "Second category description"));
        when(categoryRepository.findById(2L)).thenReturn(optionalCategory);

        when(categoryRepository.findCategoryByName("Second category")).thenReturn(Optional.empty());
        mockSave(categoryRepository);

        //When
        CategoryDto categoryDto = categoryService.update(
            2L, new CategoryUpdateDto.Request("Second category", "New second category description")
        );

        //Then
        assertEquals(categoryDto.getId(), 2L);
        assertEquals(categoryDto.getName(), "Second category");
        assertEquals(categoryDto.getDescription(), "New second category description");
    }

    @Test
    public void delete_nonExistingId_throwException() {
        //Given
        when(categoryRepository.existsById(1L)).thenReturn(false);

        //When, then
        assertThrows(CategoryNotFoundException.class, () -> categoryService.delete(1L));
    }

    @Test
    public void delete_existingId_noException() {
        //Given
        when(categoryRepository.existsById(1L)).thenReturn(true);

        //When, then
        categoryService.delete(1L);
    }

    private void mockFindAll(CategoryRepository categoryRepository, int size) {
        List<Category> categories = new ArrayList<>();
        for (long i = 1; i <= size; i++) {
            categories.add(
                of(i, "#" + i + " name", "#" + i + " category description")
            );
        }

        when(categoryRepository.findAll(any(Pageable.class))).thenAnswer(inv -> {
            Pageable pageable = inv.getArgument(0);
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), size);
            return new PageImpl<>(categories.subList(start, end), pageable, size);
        });
    }

    private void mockSave(CategoryRepository categoryRepository) {
        when(categoryRepository.save(any())).thenAnswer(inv -> {
            Category category = inv.getArgument(0);
            if (category.getId() != null) {
                return category;
            }

            category = spy(category);
            when(category.getId()).thenReturn(1L);
            return category;
        });
    }

}
package org.quizzer.category.api;

import lombok.RequiredArgsConstructor;
import org.quizzer.category.dto.base.CategoryDto;
import org.quizzer.category.dto.creation.CategoryCreationDto;
import org.quizzer.category.dto.page.PageDto;
import org.quizzer.category.dto.update.CategoryUpdateDto;
import org.quizzer.category.exceptions.CategoryNotFoundException;
import org.quizzer.category.services.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public PageDto<CategoryDto> getCategories(@PageableDefault Pageable pageable) {
        return categoryService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public CategoryDto getCategory(@PathVariable("id") Long id) throws CategoryNotFoundException {
        return categoryService.get(id);
    }

    @PostMapping
    public CategoryDto createCategory(@Valid CategoryCreationDto.Request request) {
        //TODO write tests
        return null;
    }

    @PutMapping
    public CategoryDto updateCategory(@Valid CategoryUpdateDto.Request request) {
        //TODO write tests
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable("id") Long id) {

    }
}

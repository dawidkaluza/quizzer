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
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid CategoryCreationDto.Request request) {
        return categoryService.create(request);
    }

    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable("id") Long id, @RequestBody @Valid CategoryUpdateDto.Request request) throws CategoryNotFoundException {
        return categoryService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("id") Long id) {
        categoryService.delete(id);
    }
}

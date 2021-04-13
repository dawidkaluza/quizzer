package org.quizzer.category.api;

import org.quizzer.category.dto.base.CategoryDto;
import org.quizzer.category.dto.creation.CategoryCreationDto;
import org.quizzer.category.dto.update.CategoryUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @GetMapping
    public Page<CategoryDto> getCategories() {
        //TODO write tests
        return null;
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
    public void deleteCategory(@PathVariable Long id) {

    }
}

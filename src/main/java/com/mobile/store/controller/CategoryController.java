package com.mobile.store.controller;

import com.mobile.store.domain.Category;
import com.mobile.store.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping(value = "/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Category> getCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Category not found with id: " + id)));
    }

    @PostMapping(value = "/categories", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Category> createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }
}

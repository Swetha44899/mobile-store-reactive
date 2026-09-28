package com.mobile.store.controller;

import com.mobile.store.domain.Brand;
import com.mobile.store.repository.BrandRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class BrandController {

    private final BrandRepository brandRepository;

    public BrandController(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @GetMapping(value = "/brands", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @GetMapping(value = "/brands/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Brand> getBrandById(@PathVariable Long id) {
        return brandRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Brand not found with id: " + id)));
    }

    @PostMapping(value = "/brands", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Brand> createBrand(@RequestBody Brand brand) {
        return brandRepository.save(brand);
    }
}

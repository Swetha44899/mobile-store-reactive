package com.mobile.store.controller;

import com.mobile.store.domain.Mobile;
import com.mobile.store.service.MobileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class MobileController {

    private final MobileService mobileService;

    public MobileController(MobileService mobileService) {
        this.mobileService = mobileService;
    }

    @GetMapping(value = "/mobiles", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Mobile> getAllMobiles() {
        return mobileService.getAllMobiles();
    }

    @GetMapping(value = "/mobiles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Mobile> getMobileById(@PathVariable Long id) {
        return mobileService.getMobileById(id);
    }

    @GetMapping(value = "/mobiles/brand/{brandId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Mobile> getMobilesByBrand(@PathVariable Long brandId) {
        return mobileService.getMobilesByBrand(brandId);
    }

    @GetMapping(value = "/mobiles/category/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Mobile> getMobilesByCategory(@PathVariable Long categoryId) {
        return mobileService.getMobilesByCategory(categoryId);
    }

    @PostMapping(value = "/mobiles", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Mobile> createMobile(@Valid @RequestBody Mobile mobile) {
        return mobileService.createMobile(mobile);
    }

    @PutMapping(value = "/mobiles/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Mobile> updateMobile(@PathVariable Long id, @Valid @RequestBody Mobile mobile) {
        return mobileService.updateMobile(id, mobile);
    }

    @DeleteMapping(value = "/mobiles/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMobile(@PathVariable Long id) {
        return mobileService.deleteMobile(id);
    }
}

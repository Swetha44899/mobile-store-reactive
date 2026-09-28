package com.mobile.store.service;

import com.mobile.store.domain.Mobile;
import com.mobile.store.repository.MobileRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class MobileService {

    private final MobileRepository mobileRepository;

    public MobileService(MobileRepository mobileRepository) {
        this.mobileRepository = mobileRepository;
    }

    public Flux<Mobile> getAllMobiles() {
        return mobileRepository.findAll();
    }

    public Mono<Mobile> getMobileById(Long id) {
        return mobileRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Mobile not found with id: " + id)));
    }

    public Flux<Mobile> getMobilesByBrand(Long brandId) {
        return mobileRepository.findByBrandId(brandId);
    }

    public Flux<Mobile> getMobilesByCategory(Long categoryId) {
        return mobileRepository.findByCategoryId(categoryId);
    }

    public Mono<Mobile> createMobile(Mobile mobile) {
        validate(mobile);
        LocalDateTime now = LocalDateTime.now();
        mobile.setCreatedAt(now);
        mobile.setUpdatedAt(now);
        if (mobile.getAvailable() == null) {
            mobile.setAvailable(true);
        }
        return mobileRepository.save(mobile);
    }

    public Mono<Mobile> updateMobile(Long id, Mobile mobile) {
        return mobileRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Mobile not found with id: " + id)))
                .flatMap(existing -> {
                    validate(mobile);
                    existing.setBrandId(mobile.getBrandId());
                    existing.setCategoryId(mobile.getCategoryId());
                    existing.setName(mobile.getName());
                    existing.setModel(mobile.getModel());
                    existing.setColor(mobile.getColor());
                    existing.setStorage(mobile.getStorage());
                    existing.setPrice(mobile.getPrice());
                    existing.setStockQuantity(mobile.getStockQuantity());
                    existing.setAvailable(mobile.getAvailable());
                    existing.setImageUrl(mobile.getImageUrl());
                    existing.setDescription(mobile.getDescription());
                    existing.setUpdatedAt(LocalDateTime.now());
                    return mobileRepository.save(existing);
                });
    }

    public Mono<Void> deleteMobile(Long id) {
        return mobileRepository.deleteById(id);
    }

    private void validate(Mobile mobile) {
        if (mobile == null) {
            throw new IllegalArgumentException("Mobile payload cannot be null");
        }
        if (mobile.getBrandId() == null || mobile.getCategoryId() == null) {
            throw new IllegalArgumentException("brandId and categoryId are required");
        }
        if (mobile.getName() == null || mobile.getName().isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (mobile.getModel() == null || mobile.getModel().isBlank()) {
            throw new IllegalArgumentException("model is required");
        }
        if (mobile.getPrice() == null) {
            throw new IllegalArgumentException("price is required");
        }
        if (mobile.getStockQuantity() == null) {
            throw new IllegalArgumentException("stockQuantity is required");
        }
    }
}

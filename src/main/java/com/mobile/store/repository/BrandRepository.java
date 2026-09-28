package com.mobile.store.repository;

import com.mobile.store.domain.Brand;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BrandRepository extends ReactiveCrudRepository<Brand, Long> {
}

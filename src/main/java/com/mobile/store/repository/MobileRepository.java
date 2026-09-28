package com.mobile.store.repository;

import com.mobile.store.domain.Mobile;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MobileRepository extends ReactiveCrudRepository<Mobile, Long> {
    Flux<Mobile> findByBrandId(Long brandId);
    Flux<Mobile> findByCategoryId(Long categoryId);
}

package com.mobile.store.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("brands")
public class Brand {

    @Id
    private Long id;
    private String name;
    private String country;
    private LocalDateTime createdAt;

    public Brand() {
    }

    public Brand(Long id, String name, String country, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

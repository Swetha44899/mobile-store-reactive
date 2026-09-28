package com.mobile.store.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.DatabaseClient;

import java.util.List;

@Configuration
public class DatabaseInitializer implements ApplicationRunner {

    private final DatabaseClient databaseClient;

    public DatabaseInitializer(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<String> statements = List.of(
                "CREATE TABLE IF NOT EXISTS brands (id BIGSERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL, country VARCHAR(100), created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS categories (id BIGSERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL, description VARCHAR(255), created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS mobiles (id BIGSERIAL PRIMARY KEY, brand_id BIGINT NOT NULL, category_id BIGINT NOT NULL, name VARCHAR(150) NOT NULL, model VARCHAR(100) NOT NULL, color VARCHAR(50), storage VARCHAR(50), price NUMERIC(10,2) NOT NULL, stock_quantity INTEGER NOT NULL DEFAULT 0, available BOOLEAN NOT NULL DEFAULT true, image_url VARCHAR(255), description TEXT, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS orders (id BIGSERIAL PRIMARY KEY, mobile_id BIGINT NOT NULL, customer_name VARCHAR(150) NOT NULL, quantity INTEGER NOT NULL, total_amount NUMERIC(10,2) NOT NULL, status VARCHAR(50) NOT NULL DEFAULT 'PLACED', created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)",
                "CREATE INDEX IF NOT EXISTS idx_mobiles_brand_id ON mobiles(brand_id)",
                "CREATE INDEX IF NOT EXISTS idx_mobiles_category_id ON mobiles(category_id)",
                "CREATE INDEX IF NOT EXISTS idx_orders_mobile_id ON orders(mobile_id)"
        );

        for (String statement : statements) {
            databaseClient.sql(statement)
                    .fetch()
                    .rowsUpdated()
                    .block();
        }

        databaseClient.sql("INSERT INTO brands (name, country) VALUES ('Apple', 'USA') ON CONFLICT DO NOTHING")
                .fetch()
                .rowsUpdated()
                .block();

        databaseClient.sql("INSERT INTO brands (name, country) VALUES ('Samsung', 'South Korea') ON CONFLICT DO NOTHING")
                .fetch()
                .rowsUpdated()
                .block();

        databaseClient.sql("INSERT INTO categories (name, description) VALUES ('Smartphone', 'Mobile phones and smartphones') ON CONFLICT DO NOTHING")
                .fetch()
                .rowsUpdated()
                .block();

        databaseClient.sql("INSERT INTO categories (name, description) VALUES ('Accessory', 'Smartphone accessories') ON CONFLICT DO NOTHING")
                .fetch()
                .rowsUpdated()
                .block();

        databaseClient.sql("INSERT INTO mobiles (brand_id, category_id, name, model, color, storage, price, stock_quantity, available, image_url, description) VALUES (1, 1, 'iPhone 15 Pro', '15 Pro', 'Titanium', '256GB', 1199.99, 15, true, 'https://example.com/iphone15pro.jpg', 'Apple flagship smartphone') ON CONFLICT DO NOTHING")
                .fetch()
                .rowsUpdated()
                .block();

        databaseClient.sql("INSERT INTO mobiles (brand_id, category_id, name, model, color, storage, price, stock_quantity, available, image_url, description) VALUES (2, 1, 'Samsung Galaxy S24 Ultra', 'S24 Ultra', 'Black', '512GB', 1399.99, 10, true, 'https://example.com/s24ultra.jpg', 'Samsung premium Android smartphone') ON CONFLICT DO NOTHING")
                .fetch()
                .rowsUpdated()
                .block();
    }
}

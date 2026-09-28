# Mobile Store Reactive API

A reactive Spring Boot application for a mobile store using Java 17, Spring WebFlux, and PostgreSQL.

## Features
- Reactive REST endpoints using Spring WebFlux
- PostgreSQL + R2DBC support
- Product, brand, category, and order management
- Reactive CRUD operations
- JDK 17 compatible

## Prerequisites
- Java 17
- Maven 3.9+
- Docker and Docker Compose

## Run PostgreSQL with Docker
```bash
docker compose up -d
```

## Start the application
```bash
mvn clean spring-boot:run
```

## API base path
```text
http://localhost:8080/api
```

## Main endpoints
- GET `/api/brands`
- POST `/api/brands`
- GET `/api/categories`
- POST `/api/categories`
- GET `/api/mobiles`
- GET `/api/mobiles/{id}`
- POST `/api/mobiles`
- PUT `/api/mobiles/{id}`
- DELETE `/api/mobiles/{id}`
- GET `/api/orders`
- POST `/api/orders`

## Example mobile payload
```json
{
  "brandId": 1,
  "categoryId": 1,
  "name": "iPhone 15 Pro",
  "model": "15 Pro",
  "color": "Titanium",
  "storage": "256GB",
  "price": 1199.99,
  "stockQuantity": 25,
  "available": true,
  "imageUrl": "https://example.com/iphone15pro.jpg",
  "description": "Latest Apple flagship smartphone"
}
```

# Mobile Store Reactive - Project Structure & Workflow

## 📁 Folder Structure

```
mobile-store-reactive/
├── src/
│   ├── main/
│   │   ├── java/com/mobile/store/
│   │   │   ├── MobileStoreApplication.java          # Main Spring Boot entry point
│   │   │   │
│   │   │   ├── config/
│   │   │   │   └── DatabaseInitializer.java         # Database initialization & seed data
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── BrandController.java             # Brand REST endpoints
│   │   │   │   ├── CategoryController.java          # Category REST endpoints
│   │   │   │   ├── MobileController.java            # Mobile/Phone REST endpoints
│   │   │   │   └── OrderController.java             # Order REST endpoints
│   │   │   │
│   │   │   ├── domain/
│   │   │   │   ├── Brand.java                       # Brand entity
│   │   │   │   ├── Category.java                    # Category entity
│   │   │   │   ├── Mobile.java                      # Mobile/Phone entity
│   │   │   │   └── Order.java                       # Order entity
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── BrandRepository.java             # Brand data access layer
│   │   │   │   ├── CategoryRepository.java          # Category data access layer
│   │   │   │   ├── MobileRepository.java            # Mobile data access layer
│   │   │   │   └── OrderRepository.java             # Order data access layer
│   │   │   │
│   │   │   └── service/
│   │   │       ├── MobileService.java               # Mobile business logic
│   │   │       └── OrderService.java                # Order business logic
│   │   │
│   │   └── resources/
│   │       └── application.yml                       # Application configuration
│   │
│   └── test/                                         # Test directory (optional)
│
├── pom.xml                                           # Maven dependencies & build config
├── docker-compose.yml                               # PostgreSQL Docker setup
├── README.md                                         # Project overview
└── PROJECT_STRUCTURE.md                             # This file
```

---

## 🏗️ Architecture Layers

### 1. **Controller Layer** (HTTP API)
**Location:** `controller/`
- Handles HTTP requests and responses
- Maps REST endpoints to service methods
- Uses Spring WebFlux for reactive endpoints
- Returns `Flux<T>` (streams) or `Mono<T>` (single values)

**Endpoints:**
```
GET    /api/brands                     → Get all brands
POST   /api/brands                     → Create brand
GET    /api/categories                 → Get all categories
POST   /api/categories                 → Create category
GET    /api/mobiles                    → Get all mobiles
GET    /api/mobiles/{id}               → Get mobile by ID
GET    /api/mobiles/brand/{brandId}    → Filter by brand
GET    /api/mobiles/category/{catId}   → Filter by category
POST   /api/mobiles                    → Create mobile
PUT    /api/mobiles/{id}               → Update mobile
DELETE /api/mobiles/{id}               → Delete mobile
GET    /api/orders                     → Get all orders
POST   /api/orders                     → Create order
GET    /api/orders/mobile/{mobileId}   → Get orders for mobile
```

### 2. **Service Layer** (Business Logic)
**Location:** `service/`
- Contains business logic and validations
- Orchestrates repository calls
- Returns reactive types (`Flux`, `Mono`)
- Validates input before database operations

**Key Methods:**
- `MobileService`: CRUD operations with validation
- `OrderService`: Order management with business rules

### 3. **Repository Layer** (Data Access)
**Location:** `repository/`
- Extends `ReactiveCrudRepository` for reactive operations
- Custom query methods using R2DBC
- Returns `Flux<T>` or `Mono<T>`

**Custom Queries:**
```java
MobileRepository:
  - findByBrandId(Long)      → Flux<Mobile>
  - findByCategoryId(Long)   → Flux<Mobile>

OrderRepository:
  - findByMobileId(Long)     → Flux<Order>
```

### 4. **Domain Layer** (Entities)
**Location:** `domain/`
- JPA-like entities mapped to database tables
- Annotated with `@Table` for R2DBC mapping
- Contains getters/setters and constructors

**Entities:**
- `Brand` → brands table
- `Category` → categories table
- `Mobile` → mobiles table
- `Order` → orders table

### 5. **Configuration Layer**
**Location:** `config/`
- `DatabaseInitializer` implements `ApplicationRunner`
- Creates tables on startup (CREATE TABLE IF NOT EXISTS)
- Seeds initial data (Apple, Samsung, etc.)
- Uses `DatabaseClient` for reactive SQL execution

---

## 🔄 Request-Response Workflow

### Example: Create a Mobile Phone

```
1. HTTP Request (Client)
   ↓
   POST /api/mobiles
   Content-Type: application/json
   {
     "brandId": 1,
     "categoryId": 1,
     "name": "iPhone 15",
     "model": "15",
     "color": "Black",
     "storage": "256GB",
     "price": 999.99,
     "stockQuantity": 10,
     "available": true,
     "imageUrl": "...",
     "description": "..."
   }

2. MobileController
   ↓
   @PostMapping("/mobiles")
   → createMobile(Mobile)
   → Calls MobileService.createMobile()

3. MobileService
   ↓
   → validate(Mobile)           // Check required fields
   → Set createdAt & updatedAt
   → Call mobileRepository.save(mobile)

4. MobileRepository (R2DBC)
   ↓
   → Reactive Query to PostgreSQL
   → INSERT INTO mobiles (...) VALUES (...)

5. PostgreSQL Database
   ↓
   → Insert record
   → Return generated ID

6. Response (Mono<Mobile>)
   ↓
   HTTP 201 Created
   {
     "id": 3,
     "brandId": 1,
     "categoryId": 1,
     "name": "iPhone 15",
     ...
     "createdAt": "2026-09-28T10:30:00",
     "updatedAt": "2026-09-28T10:30:00"
   }
```

---

## 🗄️ Database Schema

```sql
CREATE TABLE brands (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  country VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categories (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE mobiles (
  id BIGSERIAL PRIMARY KEY,
  brand_id BIGINT NOT NULL,              -- Foreign key to brands
  category_id BIGINT NOT NULL,           -- Foreign key to categories
  name VARCHAR(150) NOT NULL,
  model VARCHAR(100) NOT NULL,
  color VARCHAR(50),
  storage VARCHAR(50),
  price NUMERIC(10,2) NOT NULL,
  stock_quantity INTEGER NOT NULL DEFAULT 0,
  available BOOLEAN NOT NULL DEFAULT true,
  image_url VARCHAR(255),
  description TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders (
  id BIGSERIAL PRIMARY KEY,
  mobile_id BIGINT NOT NULL,             -- Foreign key to mobiles
  customer_name VARCHAR(150) NOT NULL,
  quantity INTEGER NOT NULL,
  total_amount NUMERIC(10,2) NOT NULL,
  status VARCHAR(50) NOT NULL DEFAULT 'PLACED',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX idx_mobiles_brand_id ON mobiles(brand_id);
CREATE INDEX idx_mobiles_category_id ON mobiles(category_id);
CREATE INDEX idx_orders_mobile_id ON orders(mobile_id);
```

---

## 🔌 Technology Stack

| Component | Technology | Purpose |
|-----------|-----------|---------|
| Framework | Spring Boot 3.3.x | Application framework |
| Reactive | Spring WebFlux | Non-blocking REST API |
| JDK | 17 | Java runtime |
| Database | PostgreSQL | Data persistence |
| Data Access | R2DBC | Reactive database driver |
| Build | Maven | Dependency management |
| Container | Docker Compose | Database containerization |

---

## 🚀 Application Startup Flow

```
1. JVM starts MobileStoreApplication.main()
   ↓
2. Spring Boot initializes context
   ↓
3. DatabaseInitializer.run() executes (ApplicationRunner)
   ↓
4. Creates tables (CREATE TABLE IF NOT EXISTS)
   ↓
5. Seeds initial data:
   - Brands: Apple, Samsung
   - Categories: Smartphone, Accessory
   - Mobiles: iPhone 15 Pro, Galaxy S24 Ultra
   ↓
6. Spring WebFlux starts listening on port 8080
   ↓
7. Ready to accept HTTP requests
```

---

## 🔀 Reactive Stream Processing

### Flux (Multiple Values)
```java
// Returns all mobiles as stream
mobileRepository.findAll()
  → Emits each mobile as it arrives
  → Non-blocking
  → Backpressure support
```

### Mono (Single Value)
```java
// Returns single mobile or error
mobileRepository.findById(id)
  → Emits one mobile OR empty/error
  → Non-blocking
  → Composable with flatMap, map, etc.
```

### Chaining Operations
```java
mobileRepository.findById(id)
  .switchIfEmpty(Mono.error(...))      // Handle not found
  .flatMap(existing -> {                 // Transform
    validate(mobile);
    existing.setName(mobile.getName());
    return mobileRepository.save(existing);
  })
  .subscribe();                          // Subscribe to stream
```

---

## 📋 Key Features

✅ **Reactive:** Non-blocking I/O with Spring WebFlux
✅ **R2DBC:** Reactive database access to PostgreSQL
✅ **Validation:** Input validation in service layer
✅ **Error Handling:** Exception handling with Mono.error()
✅ **Auto-initialization:** Database tables created on startup
✅ **Seed Data:** Sample brands, categories, mobiles pre-loaded
✅ **REST API:** Complete CRUD endpoints
✅ **Docker Support:** PostgreSQL via Docker Compose
✅ **JDK 17:** Modern Java features (records, pattern matching ready)

---

## 🛠️ Development Workflow

```
1. Client sends HTTP request to Controller
   ↓
2. Controller validates HTTP layer concerns
   ↓
3. Service layer validates business logic
   ↓
4. Repository performs database operation
   ↓
5. Reactive stream processes result
   ↓
6. Response sent back to client
```

---

## 📚 Dependencies Flow

```
MobileStoreApplication
├── MobileController
│   └── MobileService
│       └── MobileRepository (R2DBC)
│           └── PostgreSQL
│
├── OrderController
│   └── OrderService
│       └── OrderRepository (R2DBC)
│           └── PostgreSQL
│
├── BrandController
│   └── BrandRepository (R2DBC)
│       └── PostgreSQL
│
└── CategoryController
    └── CategoryRepository (R2DBC)
        └── PostgreSQL
```

---

## 🎯 Next Steps to Extend

1. **Add Error Handling:** Global exception handler
2. **Add Security:** JWT authentication with Spring Security
3. **Add Validation:** Bean validation annotations (@Valid, @NotNull)
4. **Add Logging:** SLF4J logging across layers
5. **Add Tests:** Unit tests for services, integration tests for controllers
6. **Add API Documentation:** Swagger/OpenAPI with Springdoc
7. **Add Caching:** Redis for performance
8. **Add Metrics:** Actuator for monitoring

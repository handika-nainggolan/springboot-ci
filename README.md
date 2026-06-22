# 🚀 Spring Boot CI/CD Demo

Project Spring Boot dengan CI Pipeline lengkap menggunakan GitHub Actions.

## 📁 Struktur Project

```
springboot-ci/
├── .github/
│   └── workflows/
│       └── ci.yml              # ← CI Pipeline utama
|             └── cd.yml        # ← CD Pipeline utama
├── src/
│   ├── main/java/com/example/demo/
│   │   ├── DemoApplication.java
│   │   ├── controller/
│   │   │   └── ProductController.java
│   │   ├── service/
│   │   │   └── ProductService.java
│   │   ├── model/
│   │   │   └── Product.java
│   │   └── repository/
│   │       └── ProductRepository.java
│   └── test/java/com/example/demo/
│       ├── controller/
│       │   └── ProductControllerTest.java
│       └── service/
│           └── ProductServiceTest.java
├── Dockerfile
├── checkstyle.xml
├── owasp-suppressions.xml
└── pom.xml
```

## 🔄 CI Pipeline

```
Push / PR
    │
    ▼
┌─────────────┐
│ 🔍 LINT     │  Checkstyle - validasi code style
└──────┬──────┘
       │ (parallel)
   ┌───┴────────────┐
   ▼                ▼
┌────────┐    ┌──────────────┐
│🧪 TEST │    │ 🔒 SECURITY  │
│JUnit   │    │ OWASP Check  │
│JaCoCo  │    │              │
└───┬────┘    └──────┬───────┘
    └────────┬───────┘
             ▼
      ┌─────────────┐
      │ 🐳 DOCKER   │  Build & Push (main branch only)
      └─────────────┘
             │
             ▼
      ┌─────────────┐
      │ 📊 SUMMARY  │  CI Report
      └─────────────┘
```

## ⚙️ Fitur CI

| Fitur | Tool | Keterangan |
|-------|------|------------|
| 🔍 Code Linting | Checkstyle | Validasi style & formatting kode Java |
| 🧪 Unit Testing | JUnit 5 + Mockito | 12+ test cases untuk service & controller |
| 📊 Code Coverage | JaCoCo | Minimum 70% line coverage wajib terpenuhi |
| 🔒 Security Scan | OWASP Dependency Check | Scan CVE pada dependency, fail jika CVSS ≥ 7 |
| 🐳 Docker Build | Docker Buildx | Multi-stage build, push ke Docker Hub |

## 🚀 Cara Setup

### 1. Clone & Push ke GitHub
```bash
git init
git add .
git commit -m "Initial commit: Spring Boot CI project"
git remote add origin https://github.com/USERNAME/springboot-ci.git
git push -u origin main
```

### 2. Setup GitHub Secrets
Pergi ke **Settings → Secrets and variables → Actions**, tambahkan:

| Secret | Keterangan |
|--------|------------|
| `DOCKER_USERNAME` | Username Docker Hub kamu |
| `DOCKER_TOKEN` | Access Token Docker Hub |
| `NVD_API_KEY` | API Key dari https://nvd.nist.gov/developers/request-an-api-key (opsional, tapi disarankan) |

### 3. Jalankan Lokal
```bash
# Run aplikasi
mvn spring-boot:run

# Run tests
mvn test

# Run checkstyle
mvn checkstyle:check

# Build Docker image
docker build -t springboot-demo .
docker run -p 8080:8080 springboot-demo
```

## 📡 API Endpoints

| Method | Endpoint | Deskripsi |
|--------|----------|-----------|
| GET | `/api/products` | Get semua produk |
| GET | `/api/products/{id}` | Get produk by ID |
| POST | `/api/products` | Buat produk baru |
| PUT | `/api/products/{id}` | Update produk |
| DELETE | `/api/products/{id}` | Hapus produk |
| GET | `/api/products/search?name=X` | Cari produk |

## 📦 Contoh Request

```bash
# Buat produk
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Laptop","description":"Gaming Laptop","price":15000000,"stock":10}'

# Get semua produk
curl http://localhost:8080/api/products

# Search
curl http://localhost:8080/api/products/search?name=laptop
```

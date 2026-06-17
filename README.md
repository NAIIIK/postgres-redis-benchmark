# PostgreSQL-Redis Benchmark Utility

---

A Java application that compares read performance between **PostgreSQL** and **Redis**, 
fully containerized with **Docker Compose**.

The application runs a configurable number of comparisons with a defined pause between each.
The number of comparisons and the interval are set via environment variables
`CHECK_QUANTITY` and `CHECK_PAUSE_SECONDS`.

---

## Stack

Java • Hibernate • Lettuce • Docker Compose • PostgreSQL • Redis

---

## Getting Started

### 1. Clone the repository

### 2. Build the JAR

```bash
mvn clean package
```

### 3. Configure database credentials

Edit `docker-compose.yml`:

* set `POSTGRES_USER` and `POSTGRES_PASSWORD` in the `postgres` service
* set `DB_USER` and `DB_PASSWORD` in the `app` service.

### 4. Build and start all services

```bash
docker compose up --build
```

### 5. Stop services

```bash
docker compose down
```

---

## Environment Variables

| Переменная            | Значение по умолчанию | Описание                             |
|-----------------------|-----------------------|--------------------------------------|
| `DB_NAME`             | `final4`              | Database name                        |
| `DB_HOST`             | `postgres`            | PostgreSQL host (service name)       |
| `DB_PORT`             | `5432`                | PostgreSQL port                      |
| `DB_USER`             | `null`                | Database user                        |
| `DB_PASSWORD`         | `null`                | Database password                    |
| `REDIS_HOST`          | `redis`               | Redis host (service name)            |
| `REDIS_PORT`          | `6379`                | Redis port                           |
| `CHECK_QUANTITY` | `3`                   | Number of comparisons to run                 |
| `CHECK_PAUSE_SECONDS` | `3`                   | Pause in seconds between comparisons |

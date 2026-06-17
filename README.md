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

| Переменная            | Значение по умолчанию | Описание |
|-----------------------|-----------------------|----------|
| `DB_NAME`             | `final4`              | Название базы данных |
| `DB_HOST`             | `postgres`            | Хост PostgreSQL (имя сервиса) |
| `DB_PORT`             | `5432`                | Порт PostgreSQL |
| `DB_USER`             | `null`                | Пользователь базы данных |
| `DB_PASSWORD`         | `null`                | Пароль базы данных |
| `REDIS_HOST`          | `redis`               | Хост Redis (имя сервиса) |
| `REDIS_PORT`          | `6379`                | Порт Redis |
| `CHECK_QUANTITY` | `3`                   | Количество сравнений |
| `CHECK_PAUSE_SECONDS` | `3`                   | Пауза (в секундах) между сравнениями |

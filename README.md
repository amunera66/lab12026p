[![CI/CD Pipeline](https://github.com/amunera66/lab12026p/actions/workflows/build.yml/badge.svg)](https://github.com/amunera66/lab12026p/actions/workflows/build.yml)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=amunera66_lab12026p&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=amunera66_lab12026p) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=amunera66_lab12026p&metric=coverage)](https://sonarcloud.io/summary/new_code?id=amunera66_lab12026p) [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=amunera66_lab12026p&metric=bugs)](https://sonarcloud.io/summary/new_code?id=amunera66_lab12026p) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=amunera66_lab12026p&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=amunera66_lab12026p) [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=amunera66_lab12026p&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=amunera66_lab12026p) [![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=amunera66_lab12026p&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=amunera66_lab12026p)

# banLOCOmbia

API REST bancaria construida con Spring Boot 3.5 (Java 17) y MySQL. Permite:

- **Clientes**: listar, consultar por id y crear cuentas (`/api/customers`).
- **Transferencias**: mover dinero entre cuentas validando existencia y saldo (`POST /api/transactions`).
- **Historial**: consultar las transacciones enviadas o recibidas por una cuenta (`GET /api/transactions/{accountNumber}`).

## Estructura

```
src/
├── main/java/com/udea/lab12026p/   # Código de la aplicación
│   ├── controller/                 # Endpoints REST
│   ├── service/                    # Lógica de negocio
│   ├── repository/                 # Acceso a datos (Spring Data JPA)
│   ├── entity/  dto/  mapper/      # Modelo, DTOs y mapeo (MapStruct)
│   └── config/                     # CORS
├── main/resources/                 # application.properties (+ application-local.properties, no versionado)
└── test/                           # Pruebas JUnit 5 + Mockito + MockMvc (H2 en memoria)
frontend/                           # Cliente web (React + Vite)
.github/workflows/build.yml         # Pipeline de CI
Dockerfile                          # Imagen de la API
```

## Correr en local

1. Tener MySQL corriendo con una base `lab12026p`.
2. Crear la configuración local a partir del ejemplo y poner tu contraseña:
   ```bash
   cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
   ```
   `application-local.properties` está en `.gitignore`; nunca se sube al repositorio.
3. Levantar la API:
   ```bash
   ./mvnw spring-boot:run
   ```
   Queda en `http://localhost:8080` (por ejemplo `GET /api/customers`).

En otros entornos (Docker, nube) la configuración se pasa por variables de entorno: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `PORT` y `SPRING_PROFILES_ACTIVE`.

## Pruebas

```bash
./mvnw clean verify
```

Las pruebas usan H2 en memoria (`src/test/resources/application.properties`), así que no necesitan MySQL.

El reporte de cobertura de JaCoCo queda en `target/site/jacoco/index.html`.

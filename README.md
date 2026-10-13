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
├── main/resources/                 # application.properties (versionado, sin secretos)
└── test/                           # Pruebas JUnit 5 + Mockito + MockMvc (H2 en memoria)
frontend/                           # Cliente web (React + Vite)
application-local.properties        # Credenciales locales de MySQL (en la raíz, no versionado)
.github/workflows/build.yml         # Pipeline de CI
Dockerfile                          # Imagen de la API
```

## Correr en local

1. Tener MySQL corriendo con una base `lab12026p`.
2. Crear la configuración local a partir del ejemplo y poner tu contraseña:
   ```bash
   cp application-local.properties.example application-local.properties
   ```
   El archivo va en la **raíz del proyecto** (al lado del `pom.xml`), no en `src/main/resources`. Spring lo carga con
   `spring.config.import=optional:file:./application-local.properties`, así que nunca queda dentro del jar ni de la imagen
   Docker, y está en `.gitignore`, así que nunca se sube al repositorio.
3. Levantar la API:
   ```bash
   ./mvnw spring-boot:run
   ```
   Queda en `http://localhost:8080` (por ejemplo `GET /api/customers`).

En otros entornos (Docker, nube) la configuración se pasa por variables de entorno: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `PORT` y `SPRING_PROFILES_ACTIVE`.

## Correr con Docker

```bash
./mvnw clean package -DskipTests
docker build -t lab12026p .
docker run -p 8080:8080   -e SPRING_PROFILES_ACTIVE=prod   -e DB_URL="jdbc:mysql://host.docker.internal:3306/lab12026p?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"   -e DB_USERNAME=root   -e DB_PASSWORD=tu_password   lab12026p
```

El pipeline publica la imagen en Docker Hub como `<usuario>/lab12026p:latest` y `<usuario>/lab12026p:<sha>` en cada push a `main`.

## Despliegue en la nube

La API está desplegada en Render: **https://banlocombia-api.onrender.com** (por ejemplo `GET /api/customers`).
La base de datos es **MySQL en Aiven**; las credenciales se pasan a Render como variables de entorno (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`).

Flujo del pipeline ([build.yml](.github/workflows/build.yml)):

```
tests → SonarCloud → build JAR → Docker Hub → Render → smoke test
```

1. **tests**: pruebas JUnit con H2 en memoria.
2. **SonarCloud**: análisis estático y cobertura (JaCoCo).
3. **build JAR**: empaqueta `target/lab12026p.jar` y lo publica como artifact.
4. **Docker Hub**: construye la imagen con ese jar y la publica (`latest` y `<sha>`).
5. **Render**: dispara el deploy hook para que Render tome la nueva imagen.
6. **smoke test**: espera el despliegue y verifica que `GET /api/customers` responda 200 en la nube.

Se trabaja con **trunk-based development**: los cambios se hacen en ramas cortas `feature/**` que se integran a `main` por Pull Request.
En las ramas `feature/**` (y en los PR hacia `main`) solo corre la parte de integración: tests, SonarCloud y build del JAR.
La publicación en Docker Hub, el despliegue a Render y el smoke test solo ocurren cuando el cambio se fusiona a `main`.

## Pruebas

```bash
./mvnw clean verify
```

Las pruebas usan H2 en memoria (`src/test/resources/application.properties`), así que no necesitan MySQL.

El reporte de cobertura de JaCoCo queda en `target/site/jacoco/index.html`.

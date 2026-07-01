# CursoApp — Plataforma de Cursos en Línea

API REST desarrollada con Spring Boot 4.1.0 y Java 21 para gestionar una plataforma de cursos en línea. Incluye autenticación JWT, gestión de contenido educativo, compras y progreso de usuarios.

## Stack Tecnológico

- **Backend:** Spring Boot 4.1.0, Java 21
- **Base de datos:** PostgreSQL (nube)
- **Seguridad:** Spring Security + JWT (jjwt 0.12.6)
- **Pagos:** Stripe (modo test)
- **Build:** Maven

## Configuración del entorno

Debido a un problema de compatibilidad con la lectura de archivos `.env` en IntelliJ, **se recomienda configurar los valores directamente en `application.yaml`** para desarrollo local.

### Pasos

1. Abre `src/main/resources/application.yaml`
2. Reemplaza las variables `${...}` con los valores reales:

```yaml
spring:
  application:
    name: cursoapp

  datasource:
    url: ${DB_URL}
    username: ${DB_USER}
    password: ${DB_PASSWORD}

  jpa:
    show-sql: ${JPA_SHOW_SQL}
    hibernate:
      ddl-auto: ${HIBERNATE_DDL_AUTO}
    properties:
      hibernate:
        dialect: ${HIBERNATE_DIALECT}

jwt:
  secret: ${JWT_SECRET}
  expiration: 86400000

stripe:
  secret-key: TU_STRIPE_SECRET_KEY
  
server:
  port: ${SERVER_PORT}
```

3. **Nunca subas el `application.yaml` con valores reales al repositorio.**

> El archivo `.env.example` está disponible como referencia de las variables necesarias.

## Ejecución

```bash
# Clonar el repositorio
git clone https://github.com/MiguelRRJose/PNC-ProyectoFinal-Grupo5.git

# Abrir en IntelliJ IDEA
# Configurar application.yaml con los valores reales
# Ejecutar CursoappApplication
```

## Endpoints principales

Base URL: `http://localhost:8081`

| Módulo | Ruta base |
|--------|-----------|
| Auth | `/api/auth` |
| Usuarios | `/api/users` |
| Cursos | `/api/courses` |
| Módulos | `/api/modules` |
| Lecciones | `/api/lections` |
| Videos | `/api/videos` |
| Archivos | `/api/files` |
| Preguntas | `/api/questions` |
| Respuestas | `/api/answers` |
| Compras | `/api/purchases` |
| Cupones | `/api/coupons` |
| Pagos | `/api/payments` |
| Completions | `/api/completions` |
| Certificados | `/api/certificates` |
| Logs | `/api/logs` |

## Equipo — Grupo 5

| Integrante | Módulo |
|------------|--------|
| Miguel | Content (modules, lections, videos, files, questions, answers) |
| Mauricio | Catalogue (courses, tags, reviews, favorites) |
| Orlando | Identity, Commerce, Progress |
| Waldo | Audit (logs) |
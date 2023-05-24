# Spring Boot Microservices

Este es un ejemplo para ver un microservicio de Geo-tracking implementado de tres formas: reactiva funcional, reactiva con anotaciones y de forma sincrona.

## Run (Docker-compose)

Para ejecutar la aplicación en un entorno local utilizando Docker

```bash
# Build and create Jar files for the microservices
mvn clean install 

# Before we continue, we'll check our build-file for syntax-errors:
docker-compose -f docker/docker-compose.yml config

# Then we can build our images, create the defined containers, and start it in one command:
docker/docker-compose.yml --project-name "spring-reactive-demo" up --build -d
```


Otros comandos:

```bash
# Stop services
docker-compose -f docker/docker-compose.yml --project-name "spring-reactive-demo" stop

# Start service
docker-compose -f docker/docker-compose.yml --project-name "spring-reactive-demo" start

# Stop and remove containers, networks
docker-compose -f docker/docker-compose.yml --project-name "spring-reactive-demo" down

# Build or rebuild services
docker-compose -f docker/docker-compose.yml --project-name "spring-reactive-demo" build

# Create and start containers
docker-compose -f docker/docker-compose.yml --project-name "spring-reactive-demo" up -d
```

## Swagger API

```bash
# API documentation for geotracking-reactive-funcional-microservice
http://localhost:8081/swagger-ui.html

# API documentation for geotracking-reactive-annotation-microservice
http://localhost:8082/swagger-ui.html

# API documentation for geotracking-synchronous-microservice
http://localhost:8083/swagger-ui.html
```
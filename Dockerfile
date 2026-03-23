FROM node:25-alpine AS frontend-builder

WORKDIR /app/frontend

COPY frontend/package*.json ./
RUN npm ci

COPY frontend/ ./
RUN npm run build

FROM maven:3.9.14-eclipse-temurin-25-alpine AS backend-builder

WORKDIR /app/backend

COPY backend/pom.xml ./
RUN mvn dependency:go-offline

COPY backend/ ./
COPY --from=frontend-builder /app/frontend/dist/. ./src/main/resources/static

RUN mvn clean package -DskipTests

FROM eclipse-temurin:25.0.2_10-jre-alpine-3.23

WORKDIR /app

COPY --from=backend-builder /app/backend/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
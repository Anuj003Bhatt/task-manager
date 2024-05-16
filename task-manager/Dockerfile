FROM maven:3.8.3-openjdk-17

WORKDIR /app

COPY . .

RUN mvn clean package

EXPOSE 8080

CMD ["java","-jar", "-Dspring.profiles.active=prod", "/app/target/task-manager-0.0.1-SNAPSHOT.jar"]
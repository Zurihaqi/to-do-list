#
# Build stage
#
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /home/app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM eclipse-temurin:17-jre-focal
COPY --from=build /home/app/target/todolist-0.0.1-SNAPSHOT.jar /usr/local/lib/todolist.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/todolist.jar"]

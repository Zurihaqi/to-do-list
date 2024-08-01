#
# Build stage
#
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /home/app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM eclipse-temurin:17-jre-focal
WORKDIR /usr/local/lib
COPY --from=build /home/app/target/to-do-list-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/to-do-list-0.0.1-SNAPSHOT.jar"]

FROM openjdk:17-alpine

ADD target/to-do-list-0.0.1-SNAPSHOT.jar to-do-list.jar

CMD ["java", "-jar", "to-do-list.jar"]
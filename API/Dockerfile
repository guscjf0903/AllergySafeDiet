FROM openjdk:17-jdk
WORKDIR ./UI

COPY build/libs/API-0.0.1-SNAPSHOT.jar ./api.jar
EXPOSE 7777

CMD ["java", "-jar", "api.jar", "--spring.profiles.active=prod"]
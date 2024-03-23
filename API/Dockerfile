FROM openjdk:17-jdk
WORKDIR ./API

COPY build/libs/API-0.0.1-SNAPSHOT.jar ./api.jar
EXPOSE 7777

ENV SPRING_PROFILES_ACTIVE=prod

CMD ["java", "-jar", "api.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]

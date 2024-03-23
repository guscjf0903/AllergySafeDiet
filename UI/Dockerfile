FROM openjdk:17-jdk
WORKDIR ./UI

COPY build/libs/UI-0.0.1-SNAPSHOT.jar ./ui.jar
EXPOSE 8888

ENV SPRING_PROFILES_ACTIVE=prod


CMD ["java", "-jar", "ui.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
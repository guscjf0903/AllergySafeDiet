FROM openjdk:17-jdk
WORKDIR ./API

COPY build/libs/API-0.0.1-SNAPSHOT.jar ./api.jar
EXPOSE 7777

ENV SPRING_PROFILES_ACTIVE=prod

ARG AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY
ENV AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}
ENV AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}


CMD ["java", "-jar", "api.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]





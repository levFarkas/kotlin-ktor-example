FROM gradle:5.3.0-jdk-alpine
WORKDIR /app
COPY . /app
USER root
RUN chown -R gradle /app
USER gradle
EXPOSE 8080
RUN ./gradlew clean build
ENTRYPOINT ./gradlew run
FROM gradle:4.7.0-jdk8-alpine
COPY . .
EXPOSE 8080
RUN gradle build
ENTRYPOINT ["gradle", "run"]
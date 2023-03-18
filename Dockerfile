FROM maven:3.8.4-openjdk-17 as BUILD
WORKDIR /kaniko/buildcontext/app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ /app/src/
RUN mvn clean install

# Start with a base image containing Java runtime
FROM openjdk:17-jdk-slim

# Add Maintainer Info
LABEL maintainer="Joby Wilson <jobyywilson@gmail.com>"

# Set the working directory to /app
WORKDIR /app

# Copy the packaged JAR file into the container at /app
ARG JAR_FILE=app/target/donkeyking-service-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "/app.jar"]

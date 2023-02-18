# Start with a base image containing Java runtime
FROM openjdk:17-oracle

# Add Maintainer Info
LABEL maintainer="Joby Wilson <jobyywilson@gmail.com>"

# Set the working directory to /app
WORKDIR /app

# Copy the packaged JAR file into the container at /app
ARG JAR_FILE=target/${project.build.finalName}.jar
COPY ${JAR_FILE} app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "/app.jar"]

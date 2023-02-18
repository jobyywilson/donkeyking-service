# Start with a base image containing Java runtime
FROM openjdk:17-oracle

# Add Maintainer Info
LABEL maintainer="Joby Wilson <jobyywilson@gmail.com>"

# Set the working directory to /app
WORKDIR /app

# Copy the packaged JAR file into the container at /app
COPY target/donkeyking-service-1.0-SNAPSHOT.jar /app

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "donkeyking-service-1.0-SNAPSHOT.jar"]

# Start with a base image containing Java runtime
FROM adoptopenjdk/openjdk17:alpine-jre

# Add Maintainer Info
LABEL maintainer="Joby Wilson <jobyywilson@gmail.com>"

# Set the working directory to /app
WORKDIR /app

# Copy the packaged JAR file into the container at /app
COPY target/${project.build.finalName}.jar /app

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "${project.build.finalName}.jar"]

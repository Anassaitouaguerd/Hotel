# Use OpenJDK 11 as the base image
FROM openjdk:11-jdk-slim

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy the PostgreSQL JDBC driver JAR file to the container
COPY lib/postgresql-42.7.4.jar /usr/src/app/lib/postgresql-42.7.4.jar

# Copy the source code into the container
COPY src/ /usr/src/app/src/

# Expose the port the application runs on
EXPOSE 8080

# Compile all Java source code files in the src/ directory
RUN mkdir -p out && javac -d out $(find src -name "*.java")

# Run the compiled Main class with PostgreSQL driver in the classpath
CMD ["java", "-cp", "out:lib/postgresql-42.7.4.jar", "Main"]

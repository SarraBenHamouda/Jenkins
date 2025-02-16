# Use a lightweight Java image instead of installing manually
FROM openjdk:11-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy a Java file or JAR (optional)
# COPY MyApp.java /app/
# COPY myapp.jar /app/

# Expose port 80 (if needed)
EXPOSE 80

# Run Java version check
CMD ["java", "-version"]
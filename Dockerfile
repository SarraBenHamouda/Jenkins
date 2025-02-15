# Utiliser Alpine comme base
FROM alpine:latest

# Installer OpenJDK 11
RUN apk add --no-cache openjdk11

# Définir JAVA_HOME (facultatif mais recommandé)
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk
ENV PATH="$JAVA_HOME/bin:$PATH"

# Exposer le port 80 (optionnel)
EXPOSE 80

# Exécuter Java
CMD ["java", "-version"]

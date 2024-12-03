#Start with a base image containing Java runtime
FROM openjdk:17-jdk-slim

# MAINTAINER instruction is deprecated in favor of using label
#Information around who maintains the image
LABEL "org.opencontainers.image.authors"="guptajay.com"

RUN (apt-get update && apt-get install -y curl) || (apk add --no-cache curl)

# Add the application's jar to the image
COPY target/config.server-0.0.1-SNAPSHOT.jar config.server-0.0.1-SNAPSHOT.jar

# execute the application
ENTRYPOINT ["java", "-jar", "config.server-0.0.1-SNAPSHOT.jar"]
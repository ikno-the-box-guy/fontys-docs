FROM openjdk:21-jdk

WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .
COPY src src

EXPOSE 8080

RUN chmod +x ./mvnw && sed -i 's/\r$//' ./mvnw

RUN /bin/sh ./mvnw clean package -Dmaven.test.skip=true

RUN mv target/*.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]
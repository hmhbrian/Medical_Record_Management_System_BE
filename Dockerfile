# ===== BUILD =====
FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace

#Maven
RUN apt-get update && apt-get install -y maven

COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -B -DskipTests package

# ===== RUNTIME =====
FROM eclipse-temurin:17-jdk
WORKDIR /app
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0"
ENV TZ=Asia/Ho_Chi_Minh
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV SPRING_PROFILES_ACTIVE=staging

COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]

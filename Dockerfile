FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn -q -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# (tuỳ chọn) tối ưu memory cho free tier
ENV JAVA_TOOL_OPTIONS="-XX:+UseSerialGC -Xmx256m -XX:MaxMetaspaceSize=128m"

EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_TOOL_OPTIONS -jar /app/app.jar"]

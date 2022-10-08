# Gradle Build
FROM public.ecr.aws/bitnami/java:11 as gradle
WORKDIR /app
COPY . .
RUN ./gradlew build

FROM public.ecr.aws/bitnami/java:11 as builder
WORKDIR /app
COPY --from=gradle /app/.build/application/libs/application.jar ./app.jar
RUN java -Djarmode=layertools -jar ./app.jar extract

FROM public.ecr.aws/bitnami/java:11
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/application/ ./
ENTRYPOINT exec java $JAVA_OPTS -Djdk.tls.client.protocols=TLSv1.2 -Duser.timezone=UTC org.springframework.boot.loader.JarLauncher
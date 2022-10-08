# Gradle Build
FROM public.ecr.aws/bitnami/java:11 as gradle
WORKDIR /app
COPY . .
RUN ./gradlew build

FROM cirrusci/wget as datadog
WORKDIR /datadog
RUN wget -O dd-java-agent.jar 'https://dtdg.co/latest-java-tracer' --no-check-certificate

FROM public.ecr.aws/bitnami/java:11
COPY --from=gradle ./app/.build/application/libs/application.jar app.jar
ENTRYPOINT exec java $JAVA_OPTS -Djdk.tls.client.protocols=TLSv1.2 -Duser.timezone=UTC -jar ./app.jar
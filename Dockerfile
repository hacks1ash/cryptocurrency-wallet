FROM openjdk:11.0-jre-slim-buster
WORKDIR /opt
ENV PORT 8080
EXPOSE 8080
COPY ./.build/application/libs/application.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -Djdk.tls.client.protocols=TLSv1.2 -Duser.timezone=UTC -jar app.jar
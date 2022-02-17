FROM openjdk:11.0-jre-slim-buster
WORKDIR /opt
COPY elastic-apm-agent-1.28.4.jar /opt/elastic-apm.jar
COPY ./.build/application/libs/application.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS \
    -Djdk.tls.client.protocols=TLSv1.2 \
    -Duser.timezone=UTC \
    -javaagent:elastic-apm.jar \
    -jar app.jar
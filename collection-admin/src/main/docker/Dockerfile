FROM apache/skywalking-base:7.0.0-es7
VOLUME /tmp

ENV SERVICE_NAME="collection-admin-1.0.0"
ADD ${SERVICE_NAME}.jar app.jar

ENTRYPOINT ["java","-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}","-jar","/app.jar"]
FROM adoptopenjdk/openjdk11:latest
ADD target/geotracking-synchronous-microservice-*.jar geotracking-synchronous-microservice.jar
EXPOSE 9013
CMD java \
 -Dcom.sun.management.jmxremote=true \
 -Dcom.sun.management.jmxremote.local.only=false \
 -Dcom.sun.management.jmxremote.authenticate=false \
 -Dcom.sun.management.jmxremote.ssl=false \
 -Djava.rmi.server.hostname=localhost \
 -Dcom.sun.management.jmxremote.port=9013 \
 -Dcom.sun.management.jmxremote.rmi.port=9013 \
 -jar geotracking-synchronous-microservice.jar

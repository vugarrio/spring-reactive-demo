FROM adoptopenjdk/openjdk11:latest
ADD target/geotracking-reactive-annotation-microservice-*.jar geotracking-reactive-annotation-microservice.jar
EXPOSE 9012
CMD java \
 -Dcom.sun.management.jmxremote=true \
 -Dcom.sun.management.jmxremote.local.only=false \
 -Dcom.sun.management.jmxremote.authenticate=false \
 -Dcom.sun.management.jmxremote.ssl=false \
 -Djava.rmi.server.hostname=localhost \
 -Dcom.sun.management.jmxremote.port=9012 \
 -Dcom.sun.management.jmxremote.rmi.port=9012 \
 -jar geotracking-reactive-annotation-microservice.jar




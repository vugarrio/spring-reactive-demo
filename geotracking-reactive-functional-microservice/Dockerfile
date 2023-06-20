FROM adoptopenjdk/openjdk11:latest
ADD target/geotracking-reactive-functional-microservice-*.jar geotracking-reactive-functional-microservice.jar
EXPOSE 9011
CMD java \
 -Dcom.sun.management.jmxremote=true \
 -Dcom.sun.management.jmxremote.local.only=false \
 -Dcom.sun.management.jmxremote.authenticate=false \
 -Dcom.sun.management.jmxremote.ssl=false \
 -Djava.rmi.server.hostname=localhost \
 -Dcom.sun.management.jmxremote.port=9011 \
 -Dcom.sun.management.jmxremote.rmi.port=9011 \
 -jar geotracking-reactive-functional-microservice.jar
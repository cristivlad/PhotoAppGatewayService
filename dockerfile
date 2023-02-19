FROM openjdk:17-oracle
VOLUME /tmp
COPY target/PhotoAppGatewayService-0.0.1-SNAPSHOT.jar ZuulApiGateway.jar
ENTRYPOINT ["java","-jar","ZuulApiGateway.jar"]
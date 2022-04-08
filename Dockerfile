FROM openjdk:8
EXPOSE 8090
ADD target/customer.jar customer.jar
ENTRYPOINT ["java", "-jar", "/customer.jar"]




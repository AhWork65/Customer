FROM openjdk:8
EXPOSE 8091
ADD target/customer.jar customer.jar
ENTRYPOINT ["java", "-jar", "/customer.jar"]




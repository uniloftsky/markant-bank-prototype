FROM openjdk:21

ADD markant-bank-prototype.jar /markant/
WORKDIR /markant

ENV JAVA_OPTIONS=""

EXPOSE 8080

CMD java -jar  $JAVA_OPTIONS markant-bank-prototype.jar

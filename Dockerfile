FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/semantic-web-ws.jar /semantic-web-ws/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/semantic-web-ws/app.jar"]

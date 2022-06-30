# playground-mq
* Run Test `mvn clean test`
* Build `mvn clean package -DskipTests`
* Run `java -jar target/playground-mq-*.jar --spring.config.additional-location=src/localconfig/ --spring.profiles.active=artemis-mq`
* Test `curl http://localhost:8080/api/send?message=0`

# Building and running locally

```
./mvnw clean package
./mvnw spring-boot:run -Dspring-boot.run.profiles=<profile>
```

Available profiles:
``commoncsv``,
``csveed``,
``opencsv``,
``supercsv``,
``univocity``


# Testing

CSV file generation:

``
curl -i -XPOST -H 'Content-Type: multipart/form-data' http://localhost:8080/import --form 'file=@<file_name>.csv'
``

Here you should verify performance test results:
 
``http://localhost:8080/actuator/metrics/http.server.requests?tag=uri%3A/import``
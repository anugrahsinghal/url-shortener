# Pre-requisites
1. Java 8+
2. Maven
3. Docker

---

# Build

```shell
mvnw clean install -Dmaven.test.skip=true
```

---

# Run

```shell
mvnw spring-boot:run

OR 

java -jar target/app.jar
```

---

# Docker Run
```shell
docker build -t infracloud .

docker run -d -p 8080:8080 infracloud
```

---

# Test
1. Create A Short URL
    ```shell
    curl --location --request POST 'http://localhost:8080/shorten' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "longUrl": "https://www.google.com"
    }'
    ```

1. Get All Checks 
    ```shell
    curl --location --request GET 'http://localhost:8080/{shortLink}'
    ```


# Limitations / Future Scope / Enhancements

1. Can Add feature to provide custom alias
1. Can add a procedure/service that regularly deletes expired urls
1. Can Use real cache services like redis/memcache insted of using in-memory caffeine


[Swagger](http://localhost:8080/swagger-ui.html)


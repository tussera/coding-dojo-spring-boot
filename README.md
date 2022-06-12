# Weather API

### Reference Documentation :book:

The API fetches weather info for a given city using [OpenWeather](https://openweathermap.org/), then return a JSON response

```
{
    "city": "Amsterdam",
    "country": "NL",
    "temperature": 290.00
}
```

#### Basic functionalities

- Get Weather Info for a given City

### Tech Stack :technologist:

- JAVA 17
- Spring Boot
- Caffeine Cache
- MapStruct
- Flyway
- Postgres
- Maven
- JUnit5 + AssertJ + WireMock
- Docker

### Running :rocket:

The application can be started using docker compose. For this follow the next steps:

- build application
```
./mvnw clean verify
```
- set the environment variable WEATHER_API_KEY. This is needed because the OpenWeather requires an API Key.
If you do not have yet, please generate the API key going to the [OpenWeather Sign up](https://openweathermap.org/appid) page.
```
export WEATHER_API_KEY=yourApiKey
```
- build and run
```
docker compose up --build
```

Done!

Now you can get weather info from cities, for example:

```
curl --request GET 'localhost:8080/api/v1/weather/Amsterdam'
```

Enjoy! :runner:
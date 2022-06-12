package com.assignment.spring.controller;

import com.assignment.spring.BaseIntegrationTest;
import com.assignment.spring.TestData;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Weather Controller Integration Test Case")
public class WeatherControllerIT extends BaseIntegrationTest {

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private WebTestClient webTestClient;

    @LocalServerPort
    private Integer port;

    @AfterEach
    public void afterEach() {
        wireMockServer.resetAll();
    }

    @Test
    public void shouldGetAmsterdamWeatherInfo() throws InterruptedException {
        wireMockServer.stubFor(
                WireMock.get(WEATHER_TEST_URL)
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBodyFile("weatherAmsterdamResponse.json")));

        sendTestRequest(); // Should send external request
        sendTestRequest(); // Should get the info from the cache
        Thread.sleep(1000); // expireAfterWriteInSeconds is configured for 1 second on test properties
        sendTestRequest(); // Should send external request again as the cache for previous request is expired
        wireMockServer.verify(
                2, // Only 2 as on of the 3 requests hit the cache
                getRequestedFor(
                    urlEqualTo(WEATHER_TEST_URL)
                )
        );
    }

    private void sendTestRequest() {
        webTestClient
                .get()
                .uri(WEATHER_WEB_TEST_URI.replace("{port}", String.valueOf(port)))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.city")
                .isEqualTo(TestData.CITY)
                .jsonPath("$.country")
                .isEqualTo(TestData.COUNTRY)
                .jsonPath("$.temperature")
                .isEqualTo("290.43");
    }
}

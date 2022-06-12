package com.assignment.spring;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(initializers = BaseIntegrationTest.Initializer.class)
public class BaseIntegrationTest {

    protected static final String WEATHER_TEST_URL = "/data/2.5/weather?q=" + TestData.CITY + "&appid=myKey";
    protected static final String WEATHER_WEB_TEST_URI = "http://localhost:{port}/api/v1/weather/" + TestData.CITY;

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            initializeWireMock(applicationContext);
        }

        private void initializeWireMock(ConfigurableApplicationContext configurableApplicationContext) {
            WireMockServer wireMockServer = new WireMockServer(new WireMockConfiguration().dynamicPort());
            wireMockServer.start();

            configurableApplicationContext.getBeanFactory().registerSingleton("wireMockServer", wireMockServer);

            configurableApplicationContext.addApplicationListener(applicationEvent -> {
                if (applicationEvent instanceof ContextClosedEvent) {
                    wireMockServer.stop();
                }
            });

            TestPropertyValues
                    .of("weather.api.url:http://localhost:" + wireMockServer.port() + "/data/2.5/weather?q={city}&appid={appid}")
                    .applyTo(configurableApplicationContext);
        }
    }
}

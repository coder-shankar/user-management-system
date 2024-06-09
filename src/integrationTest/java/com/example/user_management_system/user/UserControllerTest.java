package com.example.user_management_system.user;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestConfiguration
public class UserControllerTest {
    static Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @LocalServerPort
    private Integer port;

    @BeforeAll
    static void setup() {
        logger.info("Starting postgres {}", postgres.getHost());
        postgres.start();
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void testCreateUser() {
        var body = Map.of("username", "shankar.ghimire",
                "firstName", "shankar",
                "lastName", "ghimire",
                "email", "shankar.ghimire@mail.com",
                "dateOfBirth", "1997-02-01"
        );
        var request = given()
                .log()
                .everything()
                .body(body)
                .contentType(ContentType.JSON);

        var response = request.when()
                              .post("/api/users");

        response.then()
                .log()
                .everything()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("id", notNullValue())
                .body("username", equalTo("shankar.ghimire"))
                .body("firstName", equalTo("shankar"))
                .body("lastName", equalTo("ghimire"))
                .body("email", equalTo("shankar.ghimire@mail.com"))
                .body("dateOfBirth", equalTo("1997-02-01"));
    }

    @Test
    @Sql(scripts = "classpath:test-user-data.sql")
    void testGetAllUser() {
        var request = given()
                .log()
                .everything()
                .contentType(ContentType.JSON);

        var response = request.when()
                              .get("/api/users");

        response.then()
                .log()
                .everything()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(".", notNullValue())
                .body("content", hasSize(greaterThan(0)));
    }


    @AfterAll
    static void destroy() {
        postgres.stop();
    }


}

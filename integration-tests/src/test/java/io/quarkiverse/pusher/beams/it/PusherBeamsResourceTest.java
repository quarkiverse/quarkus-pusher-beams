package io.quarkiverse.pusher.beams.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class PusherBeamsResourceTest {

    @Test
    public void verifyPusherNotificationsProduction() {

        given().when()
                .get("/pusher-beams/active")
                .then()
                .statusCode(200)
                .body(is("true"));
    }

    @Test
    public void verifyTokenGenerationIsWorkingFine() {

        given().when()
                .get("/pusher-beams/token")
                .then()
                .statusCode(200)
                .body(is("true"));
    }

    @Test
    public void publish() {

        given().when()
                .get("/pusher-beams/publish")
                .then()
                .statusCode(200)
                .body(is("true"));
    }

}

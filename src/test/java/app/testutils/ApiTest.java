package app.testutils;

import app.api.Application;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class ApiTest extends app.testutils.DatabaseTest {

    protected Javalin app;

    @BeforeAll
    void startApi() {
        app = Application.createApp(emf).start(0);

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = app.port();
    }

    @AfterAll
    void stopApi() {
        if (app != null) {
            app.stop();
        }
    }
}
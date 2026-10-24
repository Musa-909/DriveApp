package app.api;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class AiApiTest {

    private String login(String email, String password, String endpoint) {
        String loginJson = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(email, password);

        return given()
                .contentType(ContentType.JSON)
                .body(loginJson)
                .when()
                .post("http://localhost:7070" + endpoint)
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    private String instructorToken() {
        String email = System.getenv().getOrDefault(
                "TEST_INSTRUCTOR_EMAIL", "musa@gmail.com"
        );

        String password = System.getenv("TEST_INSTRUCTOR_PASSWORD");

        if (password == null || password.isBlank()) {
            throw new IllegalStateException(
                    "Set TEST_INSTRUCTOR_PASSWORD before running the test"
            );
        }

        return login(email, password, "/api/instructors/login");
    }

    private String studentToken() {
        String email = System.getenv("TEST_STUDENT_EMAIL");
        String password = System.getenv("TEST_STUDENT_PASSWORD");

        if (email == null || email.isBlank()
                || password == null || password.isBlank()) {
            throw new IllegalStateException(
                    "Set TEST_STUDENT_EMAIL and TEST_STUDENT_PASSWORD before running the test"
            );
        }

        return login(email, password, "/api/students/login");
    }

    @Test
    void aiWithoutJWTTest() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "question": "Hvad betyder ABS i en bil?"
                        }
                        """)
                .when()
                .post("http://localhost:7070/api/ai/driving-advice")
                .then()
                .statusCode(401);
    }

    @Test
    void aiWithInvalidJWTTest() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer invalid-token")
                .body("""
                        {
                            "question": "Hvad betyder ABS i en bil?"
                        }
                        """)
                .when()
                .post("http://localhost:7070/api/ai/driving-advice")
                .then()
                .statusCode(401);
    }

    @Test
    void instructorCanUseAI() {
        String token = instructorToken();

        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body("""
                        {
                            "question": "Hvad betyder ABS i en bil?"
                        }
                        """)
                .when()
                .post("http://localhost:7070/api/ai/driving-advice")
                .then()
                .statusCode(200)
                .body("answer", notNullValue());
    }

    @Test
    void studentCanUseAI() {
        String token = studentToken();

        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body("""
                        {
                            "question": "Hvad betyder ABS i en bil?"
                        }
                        """)
                .when()
                .post("http://localhost:7070/api/ai/driving-advice")
                .then()
                .statusCode(200)
                .body("answer", notNullValue());
    }
}
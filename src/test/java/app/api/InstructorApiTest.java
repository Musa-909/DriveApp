package app.api;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class InstructorApiTest {

    // Login som eksisterende kørelærer og hent JWT-token
    private String instructorToken() {
        String email = System.getenv().getOrDefault(
                "TEST_INSTRUCTOR_EMAIL", "musa@gmail.com"
        );

        String password = System.getenv("TEST_INSTRUCTOR_PASSWORD");

        if (password == null || password.isBlank()) {
            throw new IllegalStateException(
                    "Set TEST_INSTRUCTOR_PASSWORD before running API tests"
            );
        }

        String loginJson = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(email, password);

        return given()
                .contentType("application/json")
                .body(loginJson)
                .when()
                .post("http://localhost:7070/api/instructors/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    // Opret kørelærer med gyldig JWT
    @Test
    void createInstructorTest() {

        String token = instructorToken();

        String email = "instructor-" + UUID.randomUUID() + "@test.dk";

        String instructorJson = """
                {
                    "name": "Test Instructor",
                    "email": "%s",
                    "phoneNumber": "12345678",
                    "password": "Test1234!"
                }
                """.formatted(email);

        given()
                .contentType("application/json")
                .auth().oauth2(token)
                .body(instructorJson)
                .when()
                .post("http://localhost:7070/api/instructors")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Test Instructor"))
                .body("email", equalTo(email))
                .body("phoneNumber", equalTo("12345678"));
    }

    // Opret kørelærer uden JWT
    @Test
    void createInstructorWithoutJWTTest() {

        String instructorJson = """
                {
                    "name": "Unauthorized Instructor",
                    "email": "unauthorized@test.dk",
                    "phoneNumber": "12345678",
                    "password": "Test1234!"
                }
                """;

        given()
                .contentType("application/json")
                .body(instructorJson)
                .when()
                .post("http://localhost:7070/api/instructors")
                .then()
                .statusCode(401);
    }

    // Opret kørelærer med ugyldig JWT
    @Test
    void createInstructorWithInvalidJWTTest() {

        String instructorJson = """
                {
                    "name": "Unauthorized Instructor",
                    "email": "invalid-token@test.dk",
                    "phoneNumber": "12345678",
                    "password": "Test1234!"
                }
                """;

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer invalid-token")
                .body(instructorJson)
                .when()
                .post("http://localhost:7070/api/instructors")
                .then()
                .statusCode(401);
    }

    // Hent kørelærere uden JWT
    @Test
    void getAllInstructorsWithoutJWTTest() {

        given()
                .when()
                .get("http://localhost:7070/api/instructors")
                .then()
                .statusCode(401);
    }

    // Hent kørelærere med ugyldig JWT
    @Test
    void getAllInstructorsWithInvalidJWTTest() {

        given()
                .header("Authorization", "Bearer invalid-token")
                .when()
                .get("http://localhost:7070/api/instructors")
                .then()
                .statusCode(401);
    }
}
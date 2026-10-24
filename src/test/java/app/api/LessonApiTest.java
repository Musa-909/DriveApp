package app.api;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LessonApiTest {

    // Login og hent JWT-token
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

    // Opret køretime med gyldig JWT
    @Test
    void createNewLessonTest() {

        // Login og hent token
        String token = instructorToken();

        // Ny kørelærer til testen
        String instructorJson = """
                {
                    "name": "Test Instructor",
                    "email": "lesson-test@instructor.dk",
                    "phoneNumber": "12345678",
                    "password": "Test1234!"
                }
                """;

        int instructorId = given()
                .contentType("application/json")
                .auth().oauth2(token)
                .body(instructorJson)
                .when()
                .post("http://localhost:7070/api/instructors")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .path("id");

        // Ny køretime til testen
        String lessonJson = """
                {
                    "lessonTime": "2026-10-25T14:00:00",
                    "durationMinutes": 60,
                    "lessonType": "PRACTICAL",
                    "instructorId": %d
                }
                """.formatted(instructorId);

        given()
                .contentType("application/json")
                .auth().oauth2(token)
                .body(lessonJson)
                .when()
                .post("http://localhost:7070/api/lessons")
                .then()
                .log().all()
                .statusCode(201)
                .body("durationMinutes", equalTo(60))
                .body("lessonType", equalTo("PRACTICAL"))
                .body("instructorId", equalTo(instructorId));
    }

    // Opret køretime uden JWT
    @Test
    void createLessonWithoutJWTTest() {

        String lessonJson = """
                {
                    "lessonTime": "2026-11-25T14:00:00",
                    "durationMinutes": 60,
                    "lessonType": "PRACTICAL",
                    "instructorId": 1
                }
                """;

        given()
                .contentType("application/json")
                .body(lessonJson)
                .when()
                .post("http://localhost:7070/api/lessons")
                .then()
                .statusCode(401);
    }

    // Opret køretime med ugyldig JWT
    @Test
    void createLessonWithInvalidJWTTest() {

        String lessonJson = """
                {
                    "lessonTime": "2026-11-25T14:00:00",
                    "durationMinutes": 60,
                    "lessonType": "PRACTICAL",
                    "instructorId": 1
                }
                """;

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer invalid-token")
                .body(lessonJson)
                .when()
                .post("http://localhost:7070/api/lessons")
                .then()
                .statusCode(401);
    }
}
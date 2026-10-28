package app.api;

import app.dao.InstructorDAOImpl;
import app.entities.Instructor;
import app.testutils.ApiTest;
import org.mindrot.jbcrypt.BCrypt;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LessonApiTest extends ApiTest {

    private String testInstructorEmail;
    private static final String TEST_PASSWORD = "Test1234";

    @BeforeAll
    void createInstructorForTests() {
        testInstructorEmail = "lesson-admin-" + UUID.randomUUID() + "@test.dk";

        Instructor instructor = new Instructor(
                "Test Admin",
                testInstructorEmail,
                "22222222"
        );
        instructor.setPassword(BCrypt.hashpw(TEST_PASSWORD, BCrypt.gensalt()));
        new InstructorDAOImpl(emf).create(instructor);
    }

    // Log in as an existing instructor and retrieve a JWT token
    private String instructorToken() {
        String loginJson = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(testInstructorEmail, TEST_PASSWORD);

        return given()
                .contentType("application/json")
                .body(loginJson)
                .when()
                .post("/api/instructors/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    // Create a lesson with a valid JWT
    @Test
    void createNewLessonTest() {

        // Log in and retrieve the token
        String token = instructorToken();

        // Create a new instructor for the test
        String email = "lesson-test-" + UUID.randomUUID() + "@instructor.dk";

        String instructorJson = """
                {
                    "name": "Test Instructor",
                    "email": "%s",
                    "phoneNumber": "12345678",
                    "password": "Test1234!"
                }
                """.formatted(email);

        int instructorId = given()
                .contentType("application/json")
                .auth().oauth2(token)
                .body(instructorJson)
                .when()
                .post("/api/instructors")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .extract()
                .path("id");

        // Create a new lesson for the test
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
                .post("/api/lessons")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .body("durationMinutes", equalTo(60))
                .body("lessonType", equalTo("PRACTICAL"))
                .body("instructorId", equalTo(instructorId));
    }

    // Create a lesson without a JWT
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
                .post("/api/lessons")
                .then()
                .statusCode(401);
    }

    // Create a lesson with an invalid JWT
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
                .post("/api/lessons")
                .then()
                .statusCode(401);
    }
}
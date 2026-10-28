package app.api;

import app.dao.InstructorDAOImpl;
import app.entities.Instructor;
import app.testutils.DatabaseTest;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class AiApiTest extends DatabaseTest {

    private Javalin app;

    private String testInstructorEmail;
    private String testStudentEmail;

    private static final String TEST_PASSWORD = "Test1234";

    @BeforeAll
    void startApi() {

        // This is a fake ai, so no real Gemini api calls
        app = Application.createApp(emf, question -> CompletableFuture.completedFuture(
                        "Test answer: ABS prevents wheels from locking.")
        ).start(0);

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = app.port();

        // Create test instructor
        testInstructorEmail =
                "ai-instructor-" + UUID.randomUUID() + "@test.dk";

        Instructor instructor = new Instructor(
                "Test Instructor",
                testInstructorEmail,
                "22222222"
        );

        instructor.setPassword(
                BCrypt.hashpw(TEST_PASSWORD, BCrypt.gensalt())
        );

        new InstructorDAOImpl(emf).create(instructor);

        // Create test student
        testStudentEmail =
                "ai-student-" + UUID.randomUUID() + "@test.dk";

        String studentJson = """
                {
                    "name": "Test Student",
                    "email": "%s",
                    "phoneNumber": "12345678",
                    "password": "%s"
                }
                """.formatted(testStudentEmail, TEST_PASSWORD);

        given()
                .contentType(ContentType.JSON)
                .body(studentJson)
                .when()
                .post("/api/students")
                .then()
                .statusCode(201);
    }

    @AfterAll
    void stopApi() {
        if (app != null) {
            app.stop();
        }
    }

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
                .post(endpoint)
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    private String instructorToken() {
        return login(
                testInstructorEmail,
                TEST_PASSWORD,
                "/api/instructors/login"
        );
    }

    private String studentToken() {
        return login(
                testStudentEmail,
                TEST_PASSWORD,
                "/api/students/login"
        );
    }

    // Security tests

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
                .post("/api/ai/driving-advice")
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
                .post("/api/ai/driving-advice")
                .then()
                .statusCode(401);
    }

    // AI access tests

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
                .post("/api/ai/driving-advice")
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
                .post("/api/ai/driving-advice")
                .then()
                .statusCode(200)
                .body("answer", notNullValue());
    }
}
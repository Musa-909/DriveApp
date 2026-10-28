package app.api;

import app.dao.InstructorDAOImpl;
import app.entities.Instructor;
import app.testutils.ApiTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class InstructorApiTest extends ApiTest {

    private String testInstructorEmail;
    private static final String TEST_PASSWORD = "Test1234";

    @BeforeAll
    void createInstructorForTests() {
        testInstructorEmail = "instructor-admin-" + UUID.randomUUID() + "@test.dk";

        Instructor instructor = new Instructor(
                "Test Admin",
                testInstructorEmail,
                "22222222"
        );

        instructor.setPassword(BCrypt.hashpw(TEST_PASSWORD, BCrypt.gensalt()));
        new InstructorDAOImpl(emf).create(instructor);
    }

    // Log in as the test instructor and retrieve a JWT token
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

    // Create an instructor with a valid JWT
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
                .post("/api/instructors")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Test Instructor"))
                .body("email", equalTo(email))
                .body("phoneNumber", equalTo("12345678"));
    }

    // Create an instructor without a JWT
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
                .post("/api/instructors")
                .then()
                .statusCode(401);
    }

    // Create an instructor with an invalid JWT
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
                .post("/api/instructors")
                .then()
                .statusCode(401);
    }

    // Get all instructors without a JWT
    @Test
    void getAllInstructorsWithoutJWTTest() {

        given()
                .when()
                .get("/api/instructors")
                .then()
                .statusCode(401);
    }

    // Get all instructors with an invalid JWT
    @Test
    void getAllInstructorsWithInvalidJWTTest() {

        given()
                .header("Authorization", "Bearer invalid-token")
                .when()
                .get("/api/instructors")
                .then()
                .statusCode(401);
    }
}
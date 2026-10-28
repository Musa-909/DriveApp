package app.api;

import app.dao.InstructorDAOImpl;
import app.entities.Instructor;
import app.testutils.ApiTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class BookingApiTest extends ApiTest {

    private String testInstructorEmail;
    private static final String TEST_PASSWORD = "Test1234";

    @BeforeAll
    void createInstructorForTests() {
        testInstructorEmail = "booking-admin-" + UUID.randomUUID() + "@test.dk";

        Instructor instructor = new Instructor(
                "Booking Test Admin",
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

    @Test
    void createBookingWithoutJWTTest() {

        String json = """
                {
                    "bookingTime": "2026-10-20T14:00:00",
                    "studentId": 2,
                    "lessonId": 4
                }
                """;

        given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/api/bookings")
                .then()
                .statusCode(401);
    }

    @Test
    void createBookingWithInvalidJWTTest() {

        String json = """
                {
                    "bookingTime": "2026-10-20T14:00:00",
                    "studentId": 2,
                    "lessonId": 4
                }
                """;

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer ugyldigt.token.her")
                .body(json)
                .when()
                .post("/api/bookings")
                .then()
                .statusCode(401);
    }

    @Test
    void createBookingWithValidJWTTest() {

        // Log in as an instructor
        String instructorToken = instructorToken();

        // Create a student with a unique email
        String email = "booking-" + UUID.randomUUID() + "@student.dk";

        String studentJson = """
                {
                    "name": "Booking Test Student",
                    "email": "%s",
                    "phoneNumber": "12345678",
                    "password": "Test1234"
                }
                """.formatted(email);

        int studentId = given()
                .contentType("application/json")
                .auth().oauth2(instructorToken)
                .body(studentJson)
                .when()
                .post("/api/students")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .extract()
                .path("id");

        // Create an instructor with a unique email
        String instructorEmail =
                "booking-instructor-" + UUID.randomUUID() + "@test.dk";

        String instructorJson = """
                {
                    "name": "Booking Test Instructor",
                    "email": "%s",
                    "phoneNumber": "87654321",
                    "password": "Test1234!"
                }
                """.formatted(instructorEmail);

        int instructorId = given()
                .contentType("application/json")
                .auth().oauth2(instructorToken)
                .body(instructorJson)
                .when()
                .post("/api/instructors")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .extract()
                .path("id");

        // Create a lesson
        String lessonJson = """
                {
                    "lessonTime": "2026-11-25T14:00:00",
                    "durationMinutes": 60,
                    "lessonType": "PRACTICAL",
                    "instructorId": %d
                }
                """.formatted(instructorId);

        int lessonId = given()
                .contentType("application/json")
                .auth().oauth2(instructorToken)
                .body(lessonJson)
                .when()
                .post("/api/lessons")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .extract()
                .path("id");

        // Log in as the new student
        String loginJson = """
                {
                    "email": "%s",
                    "password": "Test1234"
                }
                """.formatted(email);

        String token = given()
                .contentType("application/json")
                .body(loginJson)
                .when()
                .post("/api/students/login")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract()
                .path("token");

        // Create a booking with a valid JWT token
        String bookingJson = """
                {
                    "bookingTime": "2026-11-25T14:00:00",
                    "studentId": %d,
                    "lessonId": %d
                }
                """.formatted(studentId, lessonId);

        given()
                .contentType("application/json")
                .auth().oauth2(token)
                .body(bookingJson)
                .when()
                .post("/api/bookings")
                .then()
                .log().ifValidationFails()
                .statusCode(201);
    }
}
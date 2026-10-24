package app.api;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class BookingApiTest {

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
                .post("http://localhost:7070/api/bookings")
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
                .post("http://localhost:7070/api/bookings")
                .then()
                .statusCode(401);
    }

    @Test
    void createBookingWithValidJWTTest() {

        // Login som kørelærer
        String instructorToken = instructorToken();

        // Opret en elev med unik e-mail
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
                .post("http://localhost:7070/api/students")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .extract()
                .path("id");

        // Opret en kørelærer med unik e-mail
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
                .post("http://localhost:7070/api/instructors")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .extract()
                .path("id");

        // Opret en køretime
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
                .post("http://localhost:7070/api/lessons")
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .extract()
                .path("id");

        // Login som den nye elev
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
                .post("http://localhost:7070/api/students/login")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract()
                .path("token");

        // Opret booking med gyldig JWT-token
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
                .post("http://localhost:7070/api/bookings")
                .then()
                .log().ifValidationFails()
                .statusCode(201);
    }
}
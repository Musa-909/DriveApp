
package app.api;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class BookingApiTest {

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
                .body(studentJson)
                .when()
                .post("http://localhost:7070/api/students")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        String instructorJson = """
                {
                    "name": "Booking Test Instructor",
                    "email": "booking-instructor@test.dk",
                    "phoneNumber": "87654321"
                }
                """;

        int instructorId = given()
                .contentType("application/json")
                .body(instructorJson)
                .when()
                .post("http://localhost:7070/api/instructors")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

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
                .body(lessonJson)
                .when()
                .post("http://localhost:7070/api/lessons")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

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
                .statusCode(200)
                .extract()
                .path("token");

        String bookingJson = """
                {
                    "bookingTime": "2026-11-25T14:00:00",
                    "studentId": %d,
                    "lessonId": %d
                }
                """.formatted(studentId, lessonId);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(bookingJson)
                .when()
                .post("http://localhost:7070/api/bookings")
                .then()
                .statusCode(201);
    }
}

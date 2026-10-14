package app.api;

import org.junit.jupiter.api.Test;

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

        String json = """
            {
                "bookingTime": "2026-10-20T14:00:00",
                "studentId": 2,
                "lessonId": 5
            }
            """;

        String loginJson = """
            {
                "email": "bcrypt@student.dk",
                "password": "Test1234"
            }
            """;

        String token = given()
                .contentType("application/json")
                .body(loginJson)
                .when()
                .post("http://localhost:7070/api/students/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(json)
                .when()
                .post("http://localhost:7070/api/bookings")
                .then()
                .log().all()
                .statusCode(201);
    }

}
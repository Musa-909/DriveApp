package app.api;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class LessonApiTest {

    @Test
    void createNewLessonTest() {

        String json = """
            {
                "lessonTime": "2026-10-25T14:00:00",
                "durationMinutes": 60,
                "lessonType": "PRACTICAL",
                "instructorId": 3
            }
            """;

        given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("http://localhost:7070/api/lessons")
                .then()
                .statusCode(201)
                .log().all();
    }
}

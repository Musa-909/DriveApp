
package app.api;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LessonApiTest {

    @Test
    void createNewLessonTest() {

//ny kørelærer bare til test
        String instructorJson = """
            {
                "name": "Test Instructor",
                "email": "lesson-test@instructor.dk",
                "phoneNumber": "12345678"
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

//ny kørelærer til test
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
                .body(lessonJson)
                .when()
                .post("http://localhost:7070/api/lessons")
                .then()
                .statusCode(201)
                .body("durationMinutes", equalTo(60))
                .body("lessonType", equalTo("PRACTICAL"))
                .body("instructorId", equalTo(instructorId));
    }
}

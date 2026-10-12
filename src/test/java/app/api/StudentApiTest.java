package app.api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class StudentApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 7070;
    }


    //Students CRUD test functions
    @Test
    void healthCheckTest() {

        given()
                .when()
                .get("/api/health")
                .then()
                .statusCode(200)
                .body("status", equalTo("ok"));
    }

    @Test
    void getAllStudentsTest() {

        given()
                .when()
                .get("/api/students")
                .then()
                .statusCode(200)
                .body("$", notNullValue());

    }

    @Test
    void createStudentTest() {

        String json = """
                {
                    "name": "Test Student",
                    "email": "test@student.dk",
                    "phoneNumber": "12345678"
                }
                """;

        given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/api/students")
                .then()
                .statusCode(201)
                .body("name", equalTo("Test Student"));
    }

    @Test
    void getStudentByIdTest() {

        given()
                .pathParam("id", 1)
                .when()
                .get("/api/students/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
    }

    @Test
    void updateStudentTest() {

        String json = """
                {
                    "name": "Updated Student",
                    "email": "updated@student.dk",
                    "phoneNumber": "87654321"
                }
                """;

        given()
                .contentType("application/json")
                .body(json)
                .pathParam("id", 1)
                .when()
                .put("/api/students/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated Student"))
                .body("email", equalTo("updated@student.dk"));
    }

    @Test
    void deleteStudentTest() {

        given()
                .pathParam("id", 1)
                .when()
                .delete("/api/students/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    void getStudentByIdShouldReturn404() {

        given()
                .pathParam("id", 99999)
                .when()
                .get("/api/students/{id}")
                .then()
                .statusCode(404)
                .body("message", equalTo("Student not found"));
    }


    //Instructor CRUD test functions
    @Test
    void getAllInstructorsTest() {
        given()
                .when()
                .get("/api/instructors")
                .then()
                .statusCode(200)
                .body("$", notNullValue());
    }


    @Test
    void createInstructorTest() {

        String json = """
                {
                    "name": "Test Instructor",
                    "email": "test@Instructor.dk",
                    "phoneNumber": "22222222"
                }
                """;

        given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/api/instructors")
                .then()
                .statusCode(201)
                .body("name", equalTo("Test Instructor"));
    }

    @Test
    void getInstructorByIdTest() {
        given()
                .pathParam("id", 3)
                .when()
                .get("/api/instructors/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(3));
    }

    @Test
    void updateInstructorTest() {
        String json = """   
                {   
                  "name": "Updated Instructor",   
                  "email": "updated@instructor.dk",   
                  "phoneNumber": "88888888"   
                }   
                """;

        given()
                .contentType("application/json")
                .body(json)
                .pathParam("id", 3)
                .when()
                .put("/api/instructors/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated Instructor"))
                .body("email", equalTo("updated@instructor.dk"));
    }

    @Test
    void deleteInstructorTest() {
        given()
                .pathParam("id", 3)
                .when()
                .get("/api/instructors/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(3));
    }


    // Lesson CRUD test functions

    @Test
    void getAllLessonsTest() {
        given()
                .when()
                .get("/api/lessons")
                .then()
                .statusCode(200)
                .body("$", notNullValue());
    }

    @Test
    void createLessonTest() {

        String json = """  
                {  
                  "lessonTime": "2026-10-10T12:00:00",  
                  "durationMinutes": 45,  
                  "lessonType": "PRACTICAL",  
                  "instructorId": 3  
                }  
                """;

        given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/api/lessons")
                .then()
                .statusCode(201)
                .body("durationMinutes", equalTo(45))
                .body("lessonType", equalTo("PRACTICAL"));
    }

    @Test
    void getLessonByIdTest() {
        given()
                .pathParam("id", 3)
                .when()
                .get("/api/lessons/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(3));
    }

    @Test
    void updateLessonTest() {

        String json = """  
                {  
                  "lessonTime": "2026-10-10T14:00:00",  
                  "durationMinutes": 60,  
                  "lessonType": "THEORY",  
                  "instructorId": 3  
                }  
                """;

        given()
                .contentType("application/json")
                .body(json)
                .pathParam("id", 3)
                .when()
                .put("/api/lessons/{id}")
                .then()
                .statusCode(200)
                .body("durationMinutes", equalTo(60))
                .body("lessonType", equalTo("THEORY"));
    }

    @Test
    void deleteLessonTest() {
        given()
                .pathParam("id", 3)
                .when()
                .delete("/api/lessons/{id}")
                .then()
                .statusCode(204);
    }




    
    // Booking CRUD test functions

    @Test
    void getAllBookingsTest() {
        given()
                .when()
                .get("/api/bookings")
                .then()
                .statusCode(200)
                .body("$", notNullValue());
    }

    @Test
    void createBookingTest() {

        String json = """
                {
                  "bookingTime": "2026-10-06T19:30:00",
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
                .log().all()
                .statusCode(201)
                .body("studentId", equalTo(2))
                .body("lessonId", equalTo(4));
    }

    @Test
    void getBookingByIdTest() {
        given()
                .pathParam("id", 2)
                .when()
                .get("/api/bookings/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(2));
    }

    @Test
    void updateBookingTest() {

        String json = """
                {
                  "bookingTime": "2026-10-07T10:00:00",
                  "studentId": 2,
                  "lessonId": 4
                }
                """;

        given()
                .contentType("application/json")
                .body(json)
                .pathParam("id", 2)
                .when()
                .put("/api/bookings/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .body("studentId", equalTo(2))
                .body("lessonId", equalTo(4));
    }

    @Test
    void deleteBookingTest() {
        given()
                .pathParam("id", 2)
                .when()
                .delete("/api/bookings/{id}")
                .then()
                .statusCode(204);
    }


}
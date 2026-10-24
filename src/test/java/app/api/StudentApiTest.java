
package app.api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.mindrot.jbcrypt.BCrypt;
import app.dao.StudentDAOImpl;
import app.entities.Student;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

class StudentApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 7070;
    }

    private String instructorToken() {
        String email = System.getenv().getOrDefault("TEST_INSTRUCTOR_EMAIL", "musa@gmail.com");
        String password = System.getenv("TEST_INSTRUCTOR_PASSWORD");
        if (password == null) {
            throw new IllegalStateException("Set TEST_INSTRUCTOR_PASSWORD before running API tests");
        }
        return given().contentType("application/json")
                .body("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}")
                .post("/api/instructors/login")
                .then().statusCode(200).extract().path("token");
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
                .header("Authorization", "Bearer " + instructorToken())
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
                    "email": "test-%s@student.dk",
                    "phoneNumber": "12345678",
                    "password": "Test1234"
                }
                """.formatted(UUID.randomUUID());

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
        int id = createTestStudent();

        given()
                .header("Authorization", "Bearer " + instructorToken())
                .pathParam("id", id)
                .when()
                .get("/api/students/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(id));
    }

    @Test
    void updateStudentTest() {
        int id = createTestStudent();

        String json = """
                {
                    "name": "Updated Student",
                    "email": "updated@student.dk",
                    "phoneNumber": "87654321"
                }
                """;

        given()
                .header("Authorization", "Bearer " + instructorToken())
                .contentType("application/json")
                .body(json)
                .pathParam("id", id)
                .when()
                .put("/api/students/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated Student"))
                .body("email", equalTo("updated@student.dk"));
    }

    @Test
    void deleteStudentTest() {
        int id = createTestStudent();

        given()
                .header("Authorization", "Bearer " + instructorToken())
                .pathParam("id", id)
                .when()
                .delete("/api/students/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    void getStudentByIdShouldReturn404() {

        given()
                .header("Authorization", "Bearer " + instructorToken())
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
                .header("Authorization", "Bearer " + instructorToken())
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
                    "email": "test-%s@instructor.dk",
                    "phoneNumber": "22222222",
                    "password": "Test1234"
                }
                """.formatted(UUID.randomUUID());

        given()
                .header("Authorization", "Bearer " + instructorToken())
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
        int id = createTestInstructor();

        given()
                .header("Authorization", "Bearer " + instructorToken())
                .pathParam("id", id)
                .when()
                .get("/api/instructors/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(id));
    }

    @Test
    void updateInstructorTest() {
        int id = createTestInstructor();

        String json = """
                {
                  "name": "Updated Instructor",
                  "email": "updated@instructor.dk",
                  "phoneNumber": "88888888",
                  "password": "Test1234"
                }
                """;

        given()
                .header("Authorization", "Bearer " + instructorToken())
                .contentType("application/json")
                .body(json)
                .pathParam("id", id)
                .when()
                .put("/api/instructors/{id}")
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated Instructor"))
                .body("email", equalTo("updated@instructor.dk"));
    }

    @Test
    void deleteInstructorTest() {
        int id = createTestInstructor();

        given()
                .header("Authorization", "Bearer " + instructorToken())
                .pathParam("id", id)
                .when()
                .get("/api/instructors/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(id));
    }


    // Lesson CRUD test functions

    @Test
    void getAllLessonsTest() {
        given()
                .header("Authorization", "Bearer " + instructorToken())
                .when()
                .get("/api/lessons")
                .then()
                .statusCode(200)
                .body("$", notNullValue());
    }

    @Test
    void createLessonTest() {
        int instructorId = createTestInstructor();

        String json = """
                {
                  "lessonTime": "2026-10-10T12:00:00",
                  "durationMinutes": 45,
                  "lessonType": "PRACTICAL",
                  "instructorId": %d
                }
                """.formatted(instructorId);

        given()
                .header("Authorization", "Bearer " + instructorToken())
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
        int id = createTestLesson();

        given()
                .header("Authorization", "Bearer " + instructorToken())
                .pathParam("id", id)
                .when()
                .get("/api/lessons/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(id));
    }

    @Test
    void updateLessonTest() {
        int id = createTestLesson();
        int instructorId = createTestInstructor();

        String json = """
                {
                  "lessonTime": "2026-10-10T14:00:00",
                  "durationMinutes": 60,
                  "lessonType": "THEORY",
                  "instructorId": %d
                }
                """.formatted(instructorId);

        given()
                .header("Authorization", "Bearer " + instructorToken())
                .contentType("application/json")
                .body(json)
                .pathParam("id", id)
                .when()
                .put("/api/lessons/{id}")
                .then()
                .statusCode(200)
                .body("durationMinutes", equalTo(60))
                .body("lessonType", equalTo("THEORY"));
    }

    @Test
    void deleteLessonTest() {
        int id = createTestLesson();

        given()
                .header("Authorization", "Bearer " + instructorToken())
                .pathParam("id", id)
                .when()
                .delete("/api/lessons/{id}")
                .then()
                .statusCode(204);
    }




    // Booking CRUD test functions

    @Test
    void getAllBookingsTest() {
        given()
                .header("Authorization", "Bearer " + instructorToken())
                .when()
                .get("/api/bookings")
                .then()
                .statusCode(200)
                .body("$", notNullValue());
    }

    @Test
    void createBookingTest() {
        int studentId = createTestStudent();
        int lessonId = createTestLesson();
        String token = loginTestStudent(studentId);

        String json = """
                {
                  "bookingTime": "2026-10-06T19:30:00",
                  "studentId": %d,
                  "lessonId": %d
                }
                """.formatted(studentId, lessonId);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(json)
                .when()
                .post("/api/bookings")
                .then()
                .log().all()
                .statusCode(201)
                .body("studentId", equalTo(studentId))
                .body("lessonId", equalTo(lessonId));
    }

    @Test
    void getBookingByIdTest() {
        int id = createTestBooking();

        given()
                .header("Authorization", "Bearer " + instructorToken())
                .pathParam("id", id)
                .when()
                .get("/api/bookings/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(id));
    }

    @Test
    void updateBookingTest() {
        int id = createTestBooking();
        int studentId = createTestStudent();
        int lessonId = createTestLesson();

        String json = """
                {
                  "bookingTime": "2026-10-07T10:00:00",
                  "studentId": %d,
                  "lessonId": %d
                }
                """.formatted(studentId, lessonId);

        given()
                .header("Authorization", "Bearer " + instructorToken())
                .contentType("application/json")
                .body(json)
                .pathParam("id", id)
                .when()
                .put("/api/bookings/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .body("studentId", equalTo(studentId))
                .body("lessonId", equalTo(lessonId));
    }

    @Test
    void deleteBookingTest() {
        int id = createTestBooking();

        given()
                .header("Authorization", "Bearer " + instructorToken())
                .pathParam("id", id)
                .when()
                .delete("/api/bookings/{id}")
                .then()
                .statusCode(204);
    }


    @Test
    void createStudentWithHashePasswordTest() {

        String json = """
            {
                "name": "BCrypt Test",
                "email": "bcrypt-%s@student.dk",
                "phoneNumber": "12345678",
                "password": "Test1234"
            }
            """.formatted(UUID.randomUUID());

        int studentId = given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/api/students")
                .then()
                .statusCode(201)
                .body("password", org.hamcrest.Matchers.nullValue())
                .extract()
                .path("id");

        StudentDAOImpl studentDAO = new StudentDAOImpl();
        Student student = studentDAO.getById(studentId);

        assertNotNull(student);
        assertNotNull(student.getPassword());
        assertNotEquals("Test1234", student.getPassword());
        assertTrue(BCrypt.checkpw("Test1234", student.getPassword()));
    }



    @Test
    void loginStudentTest() {
        String email = "bcrypt-" + UUID.randomUUID() + "@student.dk";
        createTestStudent(email);

        String json = """
                {
                    "email": "%s",
                    "password": "Test1234"
                }
                """.formatted(email);

        given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/api/students/login")
                .then()
                .statusCode(200)
                .body("message", equalTo("Login successful"))
                .body("token", notNullValue());
    }

    @Test
    void loginWithWrongPasswordTest() {

        String json = """
            {
                "email": "notfound-%s@student.dk",
                "password": "ForkertPassword123"
            }
            """.formatted(UUID.randomUUID());

        given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/api/students/login")
                .then()
                .statusCode(401);
    }


    @Test
    void loginStudentJWTTest() {
        String email = "bcrypt-" + UUID.randomUUID() + "@student.dk";
        createTestStudent(email);

        String json = """
                {
                    "email": "%s",
                    "password": "Test1234"
                }
                """.formatted(email);

        given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/api/students/login")
                .then()
                .statusCode(200)
                .body("message", equalTo("Login successful"))
                .body("token", notNullValue());
    }



    @Test
    void getAvailableLessonsTest() {
        int lessonId = createTestLesson();
        int studentId = createTestStudent();
        String token = loginTestStudent(studentId);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/lessons/available")
                .then()
                .statusCode(200)
                .body("id", org.hamcrest.Matchers.hasItem(lessonId));
    }

    @Test
    void studentCannotGetAllStudentsTest() {
        int studentId = createTestStudent();
        String token = loginTestStudent(studentId);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/students")
                .then()
                .statusCode(403);
    }

    private int createTestStudent() {
        return createTestStudent("student-" + UUID.randomUUID() + "@test.dk");
    }

    private int createTestStudent(String email) {
        String json = """
                {
                    "name": "Test Student",
                    "email": "%s",
                    "phoneNumber": "12345678",
                    "password": "Test1234"
                }
                """.formatted(email);

        return given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/api/students")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    private int createTestInstructor() {
        String json = """
                {
                    "name": "Test Instructor",
                    "email": "instructor-%s@test.dk",
                    "phoneNumber": "22222222",
                    "password": "Test1234"
                }
                """.formatted(UUID.randomUUID());

        return given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + instructorToken())
                .body(json)
                .when()
                .post("/api/instructors")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    private int createTestLesson() {
        int instructorId = createTestInstructor();
        String json = """
                {
                    "lessonTime": "2026-11-25T14:00:00",
                    "durationMinutes": 45,
                    "lessonType": "PRACTICAL",
                    "instructorId": %d
                }
                """.formatted(instructorId);

        return given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + instructorToken())
                .body(json)
                .when()
                .post("/api/lessons")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    private String loginTestStudent(int studentId) {
        String email = new StudentDAOImpl().getById(studentId).getEmail();

        String json = """
                {
                    "email": "%s",
                    "password": "Test1234"
                }
                """.formatted(email);

        return given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/api/students/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    private int createTestBooking() {
        int studentId = createTestStudent();
        int lessonId = createTestLesson();
        String token = loginTestStudent(studentId);
        String json = """
                {
                    "bookingTime": "2026-11-25T14:00:00",
                    "studentId": %d,
                    "lessonId": %d
                }
                """.formatted(studentId, lessonId);

        return given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(json)
                .when()
                .post("/api/bookings")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    // Security tests

    @Test
    void getAllStudentsWithoutJWTTest() {
        given()
                .when()
                .get("/api/students")
                .then()
                .statusCode(401);
    }

    @Test
    void getAllStudentsWithInvalidJWTTest() {
        given()
                .header("Authorization", "Bearer invalid-token")
                .when()
                .get("/api/students")
                .then()
                .statusCode(401);
    }

    @Test
    void getAllLessonsWithoutJWTTest() {
        given()
                .when()
                .get("/api/lessons")
                .then()
                .statusCode(401);
    }

    @Test
    void getAllLessonsWithInvalidJWTTest() {
        given()
                .header("Authorization", "Bearer invalid-token")
                .when()
                .get("/api/lessons")
                .then()
                .statusCode(401);
    }

    @Test
    void getAllBookingsWithoutJWTTest() {
        given()
                .when()
                .get("/api/bookings")
                .then()
                .statusCode(401);
    }

    @Test
    void getAllBookingsWithInvalidJWTTest() {
        given()
                .header("Authorization", "Bearer invalid-token")
                .when()
                .get("/api/bookings")
                .then()
                .statusCode(401);
    }

}

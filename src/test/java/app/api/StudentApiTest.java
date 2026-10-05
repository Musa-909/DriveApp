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
}
package app.dao;

import app.entities.Instructor;
import app.testutils.DatabaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class InstructorDAOTest extends DatabaseTest {

    private InstructorDAO instructorDAO;

    @BeforeEach
    void setUp() {
        instructorDAO = new InstructorDAOImpl(emf);
    }

    private Instructor createTestInstructor() {
        Instructor instructor = new Instructor(
                "Test Instructor",
                "instructor-" + UUID.randomUUID() + "@mail.com",
                "12345678"
        );
        return instructorDAO.create(instructor);
    }

    @Test
    void createAndGetByIdTest() {
        Instructor instructor = createTestInstructor();

        try {
            Instructor foundInstructor = instructorDAO.getById(instructor.getId());

            assertNotNull(foundInstructor);
            assertEquals("Test Instructor", foundInstructor.getName());
        } finally {
            instructorDAO.delete(instructor.getId());
        }

        assertNull(instructorDAO.getById(instructor.getId()));
    }

    @Test
    void getAllTest() {
        Instructor instructor = createTestInstructor();

        try {
            assertTrue(instructorDAO.getAll().stream()
                    .anyMatch(i -> i.getId() == instructor.getId()));
        } finally {
            instructorDAO.delete(instructor.getId());
        }
    }

    @Test
    void updateTest() {
        Instructor instructor = createTestInstructor();

        try {
            instructor.setName("Updated Instructor");
            instructorDAO.update(instructor);

            Instructor updatedInstructor = instructorDAO.getById(instructor.getId());
            assertNotNull(updatedInstructor);
            assertEquals("Updated Instructor", updatedInstructor.getName());
        } finally {
            instructorDAO.delete(instructor.getId());
        }
    }

    @Test
    void getByEmailTest() {
        Instructor instructor = createTestInstructor();

        try {
            Instructor foundInstructor = instructorDAO.getByEmail(instructor.getEmail());

            assertNotNull(foundInstructor);
            assertEquals(instructor.getId(), foundInstructor.getId());
        } finally {
            instructorDAO.delete(instructor.getId());
        }
    }
}
package app.dao;

import app.entities.Instructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InstructorDAOTes {

    private InstructorDAO instructorDAO = new InstructorDAOImpl();

    @Test
    void createAndGetByIdTest() {

        Instructor instructor = new Instructor("Test Instructor", "test@mail.com", "12345678");

        instructorDAO.create(instructor);

        Instructor foundInstructor = instructorDAO.getById(instructor.getId());

        assertNotNull(foundInstructor);
        assertEquals("Test Instructor", foundInstructor.getName());

        instructorDAO.delete(instructor.getId());
    }
}
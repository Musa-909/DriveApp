package app.dao;

import app.entities.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentDAOTest {

    private StudentDAO studentDAO = new StudentDAOImpl();

    @Test
    void createAndGetByIdTest() {

        Student student = new Student(
                "Test Student",
                "student-test@mail.com",
                "12345678"
        );

        studentDAO.create(student);

        Student foundStudent = studentDAO.getById(student.getId());

        assertNotNull(foundStudent);
        assertEquals("Test Student", foundStudent.getName());

        studentDAO.delete(student.getId());

        assertNull(studentDAO.getById(student.getId()));
    }
}
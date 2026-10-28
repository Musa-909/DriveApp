package app.dao;

import app.entities.Student;
import app.testutils.DatabaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class StudentDAOTest extends DatabaseTest {

    private StudentDAO studentDAO;

    @BeforeEach
    void setUp() {
        studentDAO = new StudentDAOImpl(emf);
    }

    private Student createTestStudent() {
        Student student = new Student(
                "Test Student",
                "student-" + UUID.randomUUID() + "@mail.com",
                "12345678"
        );
        return studentDAO.create(student);
    }

    @Test
    void createAndGetByIdTest() {
        Student student = createTestStudent();

        try {
            Student foundStudent = studentDAO.getById(student.getId());

            assertNotNull(foundStudent);
            assertEquals("Test Student", foundStudent.getName());
        } finally {
            studentDAO.delete(student.getId());
        }

        assertNull(studentDAO.getById(student.getId()));
    }

    @Test
    void getAllTest() {
        Student student = createTestStudent();

        try {
            assertTrue(studentDAO.getAll().stream()
                    .anyMatch(s -> s.getId() == student.getId()));
        } finally {
            studentDAO.delete(student.getId());
        }
    }

    @Test
    void updateTest() {
        Student student = createTestStudent();

        try {
            student.setName("Updated Student");
            studentDAO.update(student);

            Student updatedStudent = studentDAO.getById(student.getId());
            assertNotNull(updatedStudent);
            assertEquals("Updated Student", updatedStudent.getName());
        } finally {
            studentDAO.delete(student.getId());
        }
    }

    @Test
    void getByEmailTest() {
        Student student = createTestStudent();

        try {
            Student foundStudent = studentDAO.getByEmail(student.getEmail());

            assertNotNull(foundStudent);
            assertEquals(student.getId(), foundStudent.getId());
        } finally {
            studentDAO.delete(student.getId());
        }
    }
}
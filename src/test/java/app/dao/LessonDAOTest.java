package app.dao;

import app.entities.Instructor;
import app.entities.Lesson;
import app.entities.LessonType;
import app.testutils.DatabaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LessonDAOTest extends DatabaseTest {

    private LessonDAO lessonDAO;
    private InstructorDAO instructorDAO;

    @BeforeEach
    void setUp() {
        lessonDAO = new LessonDAOImpl(emf);
        instructorDAO = new InstructorDAOImpl(emf);
    }

    private Instructor createTestInstructor() {
        Instructor instructor = new Instructor(
                "Test Instructor",
                "lesson-" + UUID.randomUUID() + "@mail.com",
                "12345678"
        );
        return instructorDAO.create(instructor);
    }

    private Lesson createTestLesson(Instructor instructor) {
        Lesson lesson = new Lesson(LocalDateTime.now().plusDays(1), 45, LessonType.PRACTICAL);
        lesson.setInstructor(instructor);
        return lessonDAO.create(lesson);
    }

    @Test
    void createAndGetByIdTest() {
        Instructor instructor = createTestInstructor();
        try {
            Lesson lesson = createTestLesson(instructor);
            try {
                Lesson foundLesson = lessonDAO.getById(lesson.getId());

                assertNotNull(foundLesson);
                assertEquals(45, foundLesson.getDurationMinutes());
                assertEquals(LessonType.PRACTICAL, foundLesson.getLessonType());
            } finally {
                lessonDAO.delete(lesson.getId());
            }
            assertNull(lessonDAO.getById(lesson.getId()));
        } finally {
            instructorDAO.delete(instructor.getId());
        }
    }

    @Test
    void getAllTest() {
        Instructor instructor = createTestInstructor();
        try {
            Lesson lesson = createTestLesson(instructor);
            try {
                assertTrue(lessonDAO.getAll().stream()
                        .anyMatch(l -> l.getId() == lesson.getId()));
            } finally {
                lessonDAO.delete(lesson.getId());
            }
        } finally {
            instructorDAO.delete(instructor.getId());
        }
    }

    @Test
    void updateTest() {
        Instructor instructor = createTestInstructor();
        try {
            Lesson lesson = createTestLesson(instructor);
            try {
                lesson.setDurationMinutes(60);
                lessonDAO.update(lesson);

                Lesson updatedLesson = lessonDAO.getById(lesson.getId());
                assertNotNull(updatedLesson);
                assertEquals(60, updatedLesson.getDurationMinutes());
            } finally {
                lessonDAO.delete(lesson.getId());
            }
        } finally {
            instructorDAO.delete(instructor.getId());
        }
    }

    @Test
    void getAvailableTest() {
        Instructor instructor = createTestInstructor();
        try {
            Lesson lesson = createTestLesson(instructor);
            try {
                assertTrue(lessonDAO.getAvailable().stream()
                        .anyMatch(l -> l.getId() == lesson.getId()));
            } finally {
                lessonDAO.delete(lesson.getId());
            }
        } finally {
            instructorDAO.delete(instructor.getId());
        }
    }
}
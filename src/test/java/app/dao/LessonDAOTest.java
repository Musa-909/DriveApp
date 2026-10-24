package app.dao;

import app.entities.Instructor;
import app.entities.Lesson;
import app.entities.LessonType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class LessonDAOTest {

    private LessonDAO lessonDAO = new LessonDAOImpl();
    private InstructorDAO instructorDAO = new InstructorDAOImpl();

    @Test
    void createAndGetByIdTest() {

        Instructor instructor = new Instructor("Test Instructor", "lesson-test@mail.com", "12345678");
        instructorDAO.create(instructor);

        Lesson lesson = new Lesson(LocalDateTime.now().plusDays(1), 45, LessonType.PRACTICAL);
        lesson.setInstructor(instructor);

        lessonDAO.create(lesson);

        Lesson foundLesson = lessonDAO.getById(lesson.getId());

        assertNotNull(foundLesson);
        assertEquals(45, foundLesson.getDurationMinutes());
        assertEquals(LessonType.PRACTICAL, foundLesson.getLessonType());

        lessonDAO.delete(lesson.getId());
        instructorDAO.delete(instructor.getId());
    }
}
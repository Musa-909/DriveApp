package app.dao;

import app.entities.Lesson;
import app.entities.LessonType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class LessonDaoTest {

    private LessonDAO lessonDAO = new LessonDAOImpl();

    @Test
    void createAndGetByIdTest() {

        Lesson lesson = new Lesson(LocalDateTime.now().plusDays(1), 45, LessonType.PRACTICAL);

        lessonDAO.create(lesson);

        Lesson foundLesson = lessonDAO.getById(lesson.getId());

        assertNotNull(foundLesson);
        assertEquals(45, foundLesson.getDurationMinutes());
        assertEquals(LessonType.PRACTICAL, foundLesson.getLessonType());

        lessonDAO.delete(lesson.getId());
    }
}
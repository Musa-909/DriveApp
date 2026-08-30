package app.dao;

import app.entities.Lesson;

public interface LessonDAO {

    Lesson create(Lesson lesson);

    Lesson getById(int id);

    Lesson update(Lesson lesson);

    void delete(int id);
}
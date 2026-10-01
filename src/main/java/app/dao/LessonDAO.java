package app.dao;

import app.entities.Lesson;

import java.util.List;

public interface LessonDAO {

    Lesson create(Lesson lesson);

    Lesson getById(int id);

    List<Lesson> getAll();

    Lesson update(Lesson lesson);

    void delete(int id);
}
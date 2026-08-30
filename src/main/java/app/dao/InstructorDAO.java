package app.dao;

import app.entities.Instructor;

import java.util.List;

public interface InstructorDAO {

    Instructor create(Instructor instructor);

    Instructor getById(int id);

    Instructor update(Instructor instructor);

    void delete(int id);

    List<Instructor> getAll();
}
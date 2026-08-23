package app.dao;

import app.entities.Student;

import java.util.List;

public interface StudentDAO {

    Student create(Student student);

    Student getById(int id);

    List<Student> getAll();

    Student update(Student student);

    void delete(int id);
}
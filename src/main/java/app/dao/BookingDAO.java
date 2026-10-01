package app.dao;

import app.entities.Booking;

import java.util.List;

public interface BookingDAO {

    Booking create(Booking booking);

    Booking getById(int id);

    List<Booking> getAll();

    Booking update(Booking booking);

    void delete(int id);

    boolean existsByLessonId(int lessonId);
}
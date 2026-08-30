package app.dao;

import app.entities.Booking;

public interface BookingDAO {

    Booking create(Booking booking);

    Booking getById(int id);

    Booking update(Booking booking);

    void delete(int id);
}
package app.dao;

import app.entities.Booking;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingDaoTest {

    private BookingDAO bookingDAO = new BookingDAOImpl();

    @Test
    void createAndGetByIdTest() {

        Booking booking = new Booking(LocalDateTime.now());

        bookingDAO.create(booking);

        Booking foundBooking = bookingDAO.getById(booking.getId());

        assertNotNull(foundBooking);
        assertNotNull(foundBooking.getBookingTime());

        bookingDAO.delete(booking.getId());
    }
}
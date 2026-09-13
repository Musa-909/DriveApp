package app.services;

import app.dao.BookingDAO;
import app.entities.Booking;

public class BookingService {

    private final BookingDAO bookingDAO;

    public BookingService(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    public synchronized Booking bookLesson(Booking booking) {

        int lessonId = booking.getLesson().getId();

        if (bookingDAO.existsByLessonId(lessonId)) {
            throw new IllegalStateException("Lektionenn er allerede booket");
        }

        return bookingDAO.create(booking);
    }
}
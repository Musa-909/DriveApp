package app.services;

import app.dao.BookingDAO;
import app.entities.Booking;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookingService {

    private final BookingDAO bookingDAO;

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);


    public BookingService(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    public synchronized Booking bookLesson(Booking booking) {

        int lessonId = booking.getLesson().getId();

        if (bookingDAO.existsByLessonId(lessonId)) {
            throw new IllegalStateException("Lektionenn er allerede booket");
        }

        Booking createdBooking = bookingDAO.create(booking);

        executorService.submit(() -> {System.out.println("Booking confirmation sent for booking : " + createdBooking.getId());
        });

        return createdBooking;
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
package app.dao;

import app.entities.Booking;
import app.entities.Student;
import app.entities.Lesson;
import app.entities.LessonType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BookingDAOTest {

    private BookingDAO bookingDAO = new BookingDAOImpl();
    private StudentDAO studentDAO = new StudentDAOImpl();
    private LessonDAO lessonDAO = new LessonDAOImpl();

    @Test
    void createAndGetByIdTest() {


        Student student = new Student(
                "Test Student",
                "test-" + UUID.randomUUID() + "@mail.dk",
                "12345678"
        );

        studentDAO.create(student);


        Lesson lesson = new Lesson(
                LocalDateTime.now().plusDays(1),
                45,
                LessonType.PRACTICAL
        );

        lessonDAO.create(lesson);


        Booking booking = new Booking(LocalDateTime.now());

        booking.setStudent(student);
        booking.setLesson(lesson);

        bookingDAO.create(booking);


        Booking foundBooking = bookingDAO.getById(booking.getId());

        assertNotNull(foundBooking);
        assertNotNull(foundBooking.getBookingTime());


        bookingDAO.delete(booking.getId());

        assertNull(bookingDAO.getById(booking.getId()));

        // delete testdata
        lessonDAO.delete(lesson.getId());
        studentDAO.delete(student.getId());
    }
}
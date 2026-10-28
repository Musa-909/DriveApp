package app.dao;

import app.entities.Booking;
import app.entities.Lesson;
import app.entities.LessonType;
import app.entities.Student;
import app.testutils.DatabaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BookingDAOTest extends DatabaseTest {

    private BookingDAO bookingDAO;
    private StudentDAO studentDAO;
    private LessonDAO lessonDAO;

    @BeforeEach
    void setUp() {
        bookingDAO = new BookingDAOImpl(emf);
        studentDAO = new StudentDAOImpl(emf);
        lessonDAO = new LessonDAOImpl(emf);
    }

    private Booking createTestBooking() {
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
        return bookingDAO.create(booking);
    }

    private void deleteTestBooking(Booking booking) {
        bookingDAO.delete(booking.getId());
        lessonDAO.delete(booking.getLesson().getId());
        studentDAO.delete(booking.getStudent().getId());
    }

    @Test
    void createAndGetByIdTest() {
        Booking booking = createTestBooking();

        try {
            Booking foundBooking = bookingDAO.getById(booking.getId());

            assertNotNull(foundBooking);
            assertNotNull(foundBooking.getBookingTime());
        } finally {
            deleteTestBooking(booking);
        }

        assertNull(bookingDAO.getById(booking.getId()));
    }

    @Test
    void getAllTest() {
        Booking booking = createTestBooking();

        try {
            assertTrue(bookingDAO.getAll().stream()
                    .anyMatch(b -> b.getId() == booking.getId()));
        } finally {
            deleteTestBooking(booking);
        }
    }

    @Test
    void updateTest() {
        Booking booking = createTestBooking();

        try {
            LocalDateTime updatedTime = LocalDateTime.now().plusHours(2).withNano(0);
            booking.setBookingTime(updatedTime);
            bookingDAO.update(booking);

            Booking updatedBooking = bookingDAO.getById(booking.getId());
            assertNotNull(updatedBooking);
            assertEquals(updatedTime, updatedBooking.getBookingTime());
        } finally {
            deleteTestBooking(booking);
        }
    }

    @Test
    void existsByLessonIdTest() {
        Booking booking = createTestBooking();

        try {
            assertTrue(bookingDAO.existsByLessonId(booking.getLesson().getId()));
        } finally {
            deleteTestBooking(booking);
        }

        assertFalse(bookingDAO.existsByLessonId(booking.getLesson().getId()));
    }
}
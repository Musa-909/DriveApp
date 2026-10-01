package app.api;

import app.dao.BookingDAOImpl;
import app.dao.LessonDAOImpl;
import app.dao.StudentDAOImpl;
import app.dto.BookingRequestDTO;
import app.dto.BookingResponseDTO;
import app.entities.Booking;
import app.entities.Lesson;
import app.entities.Student;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BookingController {

    private final BookingDAOImpl bookingDAO;
    private final StudentDAOImpl studentDAO;
    private final LessonDAOImpl lessonDAO;

    public void getAll(Context ctx) {
        List<BookingResponseDTO> bookings = bookingDAO.getAll()
                .stream()
                .map(this::toResponse)
                .toList();

        ctx.status(200);
        ctx.json(bookings);
    }

    public void getById(Context ctx) {
        int id = getId(ctx);
        Booking booking = bookingDAO.getById(id);

        if (booking == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse(404, "Booking not found"));
            return;
        }

        ctx.status(200);
        ctx.json(toResponse(booking));
    }

    public void create(Context ctx) {
        BookingRequestDTO request = getValidatedRequest(ctx);

        Student student = studentDAO.getById(request.studentId());

        if (student == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse(404, "Student not found"));
            return;
        }

        Lesson lesson = lessonDAO.getById(request.lessonId());

        if (lesson == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse(404, "Lesson not found"));
            return;
        }

        if (bookingDAO.existsByLessonId(request.lessonId())) {
            ctx.status(400);
            ctx.json(new ErrorResponse(400, "Lesson is already booked"));
            return;
        }

        Booking booking = new Booking(request.bookingTime());

        booking.setStudent(student);
        booking.setLesson(lesson);

        Booking savedBooking = bookingDAO.create(booking);

        ctx.status(201);
        ctx.json(toResponse(savedBooking));
    }

    public void update(Context ctx) {
        int id = getId(ctx);
        Booking booking = bookingDAO.getById(id);

        if (booking == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse(404, "Booking not found"));
            return;
        }

        BookingRequestDTO request = getValidatedRequest(ctx);

        Student student = studentDAO.getById(request.studentId());

        if (student == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse(404, "Student not found"));
            return;
        }

        Lesson lesson = lessonDAO.getById(request.lessonId());

        if (lesson == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse(404, "Lesson not found"));
            return;
        }

        booking.setBookingTime(request.bookingTime());
        booking.setStudent(student);
        booking.setLesson(lesson);

        Booking updatedBooking = bookingDAO.update(booking);

        ctx.status(200);
        ctx.json(toResponse(updatedBooking));
    }

    public void delete(Context ctx) {
        int id = getId(ctx);
        Booking booking = bookingDAO.getById(id);

        if (booking == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse(404, "Booking not found"));
            return;
        }

        bookingDAO.delete(id);
        ctx.status(204);
    }

    private int getId(Context ctx) {
        return ctx.pathParamAsClass("id", Integer.class)
                .check(id -> id > 0, "Id must be greater than 0")
                .get();
    }

    private BookingRequestDTO getValidatedRequest(Context ctx) {
        return ctx.bodyValidator(BookingRequestDTO.class)
                .check(request -> request.bookingTime() != null, "Booking time is required")
                .check(request -> request.studentId() > 0, "Student id must be greater than 0")
                .check(request -> request.lessonId() > 0, "Lesson id must be greater than 0")
                .get();
    }

    private BookingResponseDTO toResponse(Booking booking) {
        return new BookingResponseDTO(
                booking.getId(),
                booking.getBookingTime(),
                booking.getStudent().getId(),
                booking.getLesson().getId()
        );
    }
}
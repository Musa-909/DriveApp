package app;

import app.dao.BookingDAO;
import app.dao.BookingDAOImpl;
import app.dao.LessonDAO;
import app.dao.LessonDAOImpl;
import app.dao.StudentDAO;
import app.dao.StudentDAOImpl;
import app.entities.Booking;
import app.entities.Lesson;
import app.entities.Student;
import app.services.BookingService;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import app.services.DrivingAiService;



public class Main {

    public static void main(String[] args) throws Exception {

        DrivingAiService aiService = new DrivingAiService();

        aiService.askDrivingAssistant(
                "What does ABS mean in a car?"
        ).thenAccept(answer -> {
            System.out.println(answer);
        });


        BookingDAO bookingDAO = new BookingDAOImpl();
        StudentDAO studentDAO = new StudentDAOImpl();
        LessonDAO lessonDAO = new LessonDAOImpl();

        BookingService bookingService = new BookingService(bookingDAO);

        Student student1 = studentDAO.getById(1);
        Student student2 = studentDAO.getById(2);

        Lesson lesson = lessonDAO.getById(2);

        System.out.println("Student 1 exists: " + (student1 != null));
        System.out.println("Student 2 exists: " + (student2 != null));
        System.out.println("Lesson exists: " + (lesson != null));




        ExecutorService executor = Executors.newFixedThreadPool(2);



        executor.submit(() -> {
            try {Booking booking = new Booking(LocalDateTime.now());

                booking.setStudent(student1);
                booking.setLesson(lesson);

                bookingService.bookLesson(booking);

                System.out.println("Student 1 booked the lesson");

            } catch (Exception e) {
                System.out.println("Student 1 could not book the lesson: " + e.getMessage());
            }
        });

        executor.submit(() -> {
            try {Booking booking = new Booking(LocalDateTime.now());
                booking.setStudent(student2);
                booking.setLesson(lesson);

                bookingService.bookLesson(booking);

                System.out.println("Student 2 booked the lesson");

            } catch (Exception e) {
                System.out.println("Student 2 could not book the lesson: " + e.getMessage());
            }
        });

        executor.shutdown();
        bookingService.shutdown();
    }
}

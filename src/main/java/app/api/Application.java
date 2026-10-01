package app.api;

import app.api.routes.BookingRoutes;
import app.api.routes.InstructorRoutes;
import app.api.routes.LessonRoutes;
import app.api.routes.StudentRoutes;
import app.dao.BookingDAOImpl;
import app.dao.InstructorDAOImpl;
import app.dao.LessonDAOImpl;
import app.dao.StudentDAOImpl;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        StudentDAOImpl studentDAO = new StudentDAOImpl();
        InstructorDAOImpl instructorDAO = new InstructorDAOImpl();
        LessonDAOImpl lessonDAO = new LessonDAOImpl();
        BookingDAOImpl bookingDAO = new BookingDAOImpl();

        StudentController studentController = new StudentController(studentDAO);
        InstructorController instructorController = new InstructorController(instructorDAO);
        LessonController lessonController = new LessonController(lessonDAO, instructorDAO);
        BookingController bookingController = new BookingController(bookingDAO, studentDAO, lessonDAO);

        StudentRoutes studentRoutes = new StudentRoutes(studentController);
        InstructorRoutes instructorRoutes = new InstructorRoutes(instructorController);
        LessonRoutes lessonRoutes = new LessonRoutes(lessonController);
        BookingRoutes bookingRoutes = new BookingRoutes(bookingController);

        Javalin app = Javalin.create();

        app.exception(Exception.class, (exception, ctx) -> {
            logger.error(
                    "Unhandled error on {} {}",
                    ctx.method(),
                    ctx.path(),
                    exception
            );

            ctx.status(500);
            ctx.json(new ErrorResponse(500, "Internal server error"));
        });

        app.before(ctx -> {
            logger.info("{} {}", ctx.method(), ctx.path());
        });

        app.after(ctx -> {
            logger.info(
                    "{} {} -> {}",
                    ctx.method(),
                    ctx.path(),
                    ctx.status()
            );
        });

        app.get("/api/health", ctx -> {
            ctx.json(Map.of(
                    "status", "ok",
                    "message", "API is running"
            ));
        });

        studentRoutes.register(app);
        instructorRoutes.register(app);
        lessonRoutes.register(app);
        bookingRoutes.register(app);

        app.start(7070);
    }
}
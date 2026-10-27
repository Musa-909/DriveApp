package app.api;

import app.api.routes.BookingRoutes;
import app.api.routes.InstructorRoutes;
import app.api.routes.LessonRoutes;
import app.api.routes.StudentRoutes;
import app.dao.BookingDAOImpl;
import app.dao.InstructorDAOImpl;
import app.dao.LessonDAOImpl;
import app.dao.StudentDAOImpl;
import app.dto.DrivingAiRequestDTO;
import app.entities.Role;
import app.security.Filter;
import app.services.DrivingAiService;
import io.javalin.Javalin;
import io.javalin.http.HandlerType;
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

        DrivingAiService drivingAiService = new DrivingAiService();

        Javalin app = Javalin.create();

        Filter filter = new Filter();

        app.before(ctx -> {
            String path = ctx.path();

            boolean isRegistration = path.equals("/api/students") && ctx.method() == HandlerType.POST;
            boolean isStudentLogin = path.equals("/api/students/login") && ctx.method() == HandlerType.POST;
            boolean isInstructorLogin = path.equals("/api/instructors/login") && ctx.method() == HandlerType.POST;
            boolean isHealth = path.equals("/api/health");

            if (path.startsWith("/api/") && !isRegistration && !isStudentLogin && !isInstructorLogin && !isHealth) {
                filter.authenticate(ctx);
            }

            if (path.equals("/api/instructors") && ctx.method() == HandlerType.POST) {
                filter.authorize(ctx, Role.INSTRUCTOR);
            }
        });

        app.exception(Exception.class, (exception, ctx) -> {
            logger.error("Unhandled error on {} {}", ctx.method(), ctx.path(), exception);
            ctx.status(500);
            ctx.json(new ErrorResponse(500, "Internal server error"));
        });

        app.before(ctx -> logger.info("{} {}", ctx.method(), ctx.path()));
        app.after(ctx -> logger.info("{} {} -> {}", ctx.method(), ctx.path(), ctx.status()));

        app.get("/api/health", ctx -> ctx.json(Map.of("status", "ok", "message", "API is running")));

        app.post("/api/ai/driving-advice", ctx -> {
            DrivingAiRequestDTO request = ctx.bodyAsClass(DrivingAiRequestDTO.class);

            if (request.question() == null || request.question().isBlank()) {
                ctx.status(400);
                ctx.json(new ErrorResponse(400, "Question is required"));
                return;
            }

            String answer = drivingAiService.askDrivingAssistant(request.question()).join();

            ctx.json(Map.of("answer", answer));
        });

        studentRoutes.register(app);
        instructorRoutes.register(app);
        lessonRoutes.register(app);
        bookingRoutes.register(app);

        app.start(7070);
    }
}
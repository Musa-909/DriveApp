package app.api;

import app.api.routes.BookingRoutes;
import app.api.routes.InstructorRoutes;
import app.api.routes.LessonRoutes;
import app.api.routes.StudentRoutes;
import app.config.HibernateConfig;
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
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        createApp(HibernateConfig.getEntityManagerFactory()).start(7070);
    }

    public static Javalin createApp(EntityManagerFactory emf) {
        DrivingAiService drivingAiService = new DrivingAiService();

        return createApp(emf, question -> {
            try {
                return drivingAiService.askDrivingAssistant(question);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static Javalin createApp(
            EntityManagerFactory emf,
            Function<String, CompletableFuture<String>> aiService
    ) {
        StudentDAOImpl studentDAO = new StudentDAOImpl(emf);
        InstructorDAOImpl instructorDAO = new InstructorDAOImpl(emf);
        LessonDAOImpl lessonDAO = new LessonDAOImpl(emf);
        BookingDAOImpl bookingDAO = new BookingDAOImpl(emf);

        StudentController studentController = new StudentController(studentDAO);
        InstructorController instructorController = new InstructorController(instructorDAO);
        LessonController lessonController = new LessonController(lessonDAO, instructorDAO);
        BookingController bookingController = new BookingController(bookingDAO, studentDAO, lessonDAO);

        StudentRoutes studentRoutes = new StudentRoutes(studentController);
        InstructorRoutes instructorRoutes = new InstructorRoutes(instructorController);
        LessonRoutes lessonRoutes = new LessonRoutes(lessonController);
        BookingRoutes bookingRoutes = new BookingRoutes(bookingController);

        Javalin app = Javalin.create();

        Filter filter = new Filter();

        app.before(ctx -> {
            String path = ctx.path();

            boolean isRegistration = path.equals("/api/students") && ctx.method() == HandlerType.POST;
            boolean isStudentLogin = path.equals("/api/students/login") && ctx.method() == HandlerType.POST;
            boolean isInstructorLogin = path.equals("/api/instructors/login") && ctx.method() == HandlerType.POST;
            boolean isHealth = path.equals("/api/health");

            if (path.startsWith("/api/")
                    && !isRegistration
                    && !isStudentLogin
                    && !isInstructorLogin
                    && !isHealth) {
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

        app.get("/api/health", ctx ->
                ctx.json(Map.of("status", "ok", "message", "API is running"))
        );

        app.post("/api/ai/driving-advice", ctx -> {
            DrivingAiRequestDTO request = ctx.bodyAsClass(DrivingAiRequestDTO.class);

            if (request.question() == null || request.question().isBlank()) {
                ctx.status(400);
                ctx.json(new ErrorResponse(400, "Question is required"));
                return;
            }

            String answer = aiService.apply(request.question()).join();

            ctx.json(Map.of("answer", answer));
        });

        studentRoutes.register(app);
        instructorRoutes.register(app);
        lessonRoutes.register(app);
        bookingRoutes.register(app);

        return app;
    }
}
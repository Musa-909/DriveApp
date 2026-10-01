package app.api;

import app.dao.InstructorDAOImpl;
import app.dao.LessonDAOImpl;
import app.dto.LessonRequestDTO;
import app.dto.LessonResponseDTO;
import app.entities.Instructor;
import app.entities.Lesson;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class LessonController {

    private final LessonDAOImpl lessonDAO;
    private final InstructorDAOImpl instructorDAO;

    public void getAll(Context ctx) {
        List<LessonResponseDTO> lessons = lessonDAO.getAll()
                .stream()
                .map(this::toResponse)
                .toList();

        ctx.status(200);
        ctx.json(lessons);
    }

    public void getById(Context ctx) {
        int id = getId(ctx);
        Lesson lesson = lessonDAO.getById(id);

        if (lesson == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse(404, "Lesson not found"));
            return;
        }

        ctx.status(200);
        ctx.json(toResponse(lesson));
    }

    public void create(Context ctx) {
        LessonRequestDTO request = getValidatedRequest(ctx);

        Instructor instructor = instructorDAO.getById(request.instructorId());

        if (instructor == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse(404, "Instructor not found"));
            return;
        }

        Lesson lesson = new Lesson(
                request.lessonTime(),
                request.durationMinutes(),
                request.lessonType()
        );

        lesson.setInstructor(instructor);

        Lesson savedLesson = lessonDAO.create(lesson);

        ctx.status(201);
        ctx.json(toResponse(savedLesson));
    }

    public void update(Context ctx) {
        int id = getId(ctx);
        Lesson lesson = lessonDAO.getById(id);

        if (lesson == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse(404, "Lesson not found"));
            return;
        }

        LessonRequestDTO request = getValidatedRequest(ctx);

        Instructor instructor = instructorDAO.getById(request.instructorId());

        if (instructor == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse(404, "Instructor not found"));
            return;
        }

        lesson.setLessonTime(request.lessonTime());
        lesson.setDurationMinutes(request.durationMinutes());
        lesson.setLessonType(request.lessonType());
        lesson.setInstructor(instructor);

        Lesson updatedLesson = lessonDAO.update(lesson);

        ctx.status(200);
        ctx.json(toResponse(updatedLesson));
    }

    public void delete(Context ctx) {
        int id = getId(ctx);
        Lesson lesson = lessonDAO.getById(id);

        if (lesson == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse(404, "Lesson not found"));
            return;
        }

        lessonDAO.delete(id);
        ctx.status(204);
    }

    private int getId(Context ctx) {
        return ctx.pathParamAsClass("id", Integer.class)
                .check(id -> id > 0, "Id must be greater than 0")
                .get();
    }

    private LessonRequestDTO getValidatedRequest(Context ctx) {
        return ctx.bodyValidator(LessonRequestDTO.class)
                .check(request -> request.lessonTime() != null, "Lesson time is required")
                .check(request -> request.durationMinutes() > 0, "Duration must be greater than 0")
                .check(request -> request.lessonType() != null, "Lesson type is required")
                .check(request -> request.instructorId() > 0, "Instructor id must be greater than 0")
                .get();
    }

    private LessonResponseDTO toResponse(Lesson lesson) {
        return new LessonResponseDTO(
                lesson.getId(),
                lesson.getLessonTime(),
                lesson.getDurationMinutes(),
                lesson.getLessonType(),
                lesson.getInstructor().getId()
        );
    }
}
package app.api;

import app.dao.StudentDAOImpl;
import app.dto.StudentRequestDTO;
import app.dto.StudentResponseDTO;
import app.entities.Student;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class StudentController {
    private final StudentDAOImpl studentDAO;



    public void getAll(Context ctx) {
        List<StudentResponseDTO> students = studentDAO.getAll()
                .stream()
                .map(this::toResponse)
                .toList();

        ctx.status(200);
        ctx.json(students);
    }

    public void getById(Context ctx) {
        int id = getId(ctx);
        Student student = studentDAO.getById(id);

        if (student == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse(404, "Student not found"));
            return;
        }

        ctx.status(200);
        ctx.json(toResponse(student));
    }

    public void create(Context ctx) {
        StudentRequestDTO request = getValidatedRequest(ctx);

        Student student = new Student(
                request.name(),
                request.email(),
                request.phoneNumber()
        );

        Student savedStudent = studentDAO.create(student);

        ctx.status(201);
        ctx.json(toResponse(savedStudent));
    }

    public void update(Context ctx) {
        int id = getId(ctx);
        Student student = studentDAO.getById(id);

        if (student == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse(404, "Student not found"));
            return;
        }

        StudentRequestDTO request = getValidatedRequest(ctx);

        student.setName(request.name());
        student.setEmail(request.email());
        student.setPhoneNumber(request.phoneNumber());

        Student updatedStudent = studentDAO.update(student);

        ctx.status(200);
        ctx.json(toResponse(updatedStudent));
    }

    public void delete(Context ctx) {
        int id = getId(ctx);
        Student student = studentDAO.getById(id);

        if (student == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse(404, "Student not found"));
            return;
        }

        studentDAO.delete(id);
        ctx.status(204);
    }

    private int getId(Context ctx) {
        return ctx.pathParamAsClass("id", Integer.class)
                .check(id -> id > 0, "Id must be greater than 0")
                .get();
    }

    private StudentRequestDTO getValidatedRequest(Context ctx) {
        return ctx.bodyValidator(StudentRequestDTO.class)
                .check(request -> request.name() != null && !request.name().isBlank(), "Name is required")
                .check(request -> request.email() != null && !request.email().isBlank(), "Email is required")
                .check(request -> request.phoneNumber() != null && !request.phoneNumber().isBlank(), "Phone number is required")
                .get();
    }

    private StudentResponseDTO toResponse(Student student) {
        return new StudentResponseDTO(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getPhoneNumber()
        );
    }
}
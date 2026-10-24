package app.api;

import app.dao.InstructorDAOImpl;
import app.dto.InstructorRequestDTO;
import app.dto.InstructorResponseDTO;
import app.dto.LoginRequestDTO;
import app.entities.Instructor;
import app.entities.Role;
import app.security.Filter;
import app.security.JWTToken;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class InstructorController {

    private final InstructorDAOImpl instructorDAO;
    private final Filter filter = new Filter();

    public void getAll(Context ctx) {
        filter.authorize(ctx, Role.INSTRUCTOR);

        List<InstructorResponseDTO> instructors = instructorDAO.getAll()
                .stream()
                .map(this::toResponse)
                .toList();

        ctx.status(200);
        ctx.json(instructors);
    }

    public void getById(Context ctx) {
        filter.authorize(ctx, Role.INSTRUCTOR);

        int id = getId(ctx);
        Instructor instructor = instructorDAO.getById(id);

        if (instructor == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse(404, "Instructor not found"));
            return;
        }

        ctx.status(200);
        ctx.json(toResponse(instructor));
    }

    public void create(Context ctx) {
        InstructorRequestDTO request = getValidatedRequest(ctx);

        String hashedPassword = BCrypt.hashpw(
                request.password(),
                BCrypt.gensalt()
        );

        Instructor instructor = new Instructor(
                request.name(),
                request.email(),
                request.phoneNumber()
        );

        instructor.setPassword(hashedPassword);
        instructor.setRole(Role.INSTRUCTOR);

        Instructor savedInstructor = instructorDAO.create(instructor);

        ctx.status(201);
        ctx.json(toResponse(savedInstructor));
    }

    public void update(Context ctx) {
        filter.authorize(ctx, Role.INSTRUCTOR);

        int id = getId(ctx);
        Instructor instructor = instructorDAO.getById(id);

        if (instructor == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse(404, "Instructor not found"));
            return;
        }

        InstructorRequestDTO request = getValidatedRequest(ctx);

        instructor.setName(request.name());
        instructor.setEmail(request.email());
        instructor.setPhoneNumber(request.phoneNumber());

        Instructor updatedInstructor = instructorDAO.update(instructor);

        ctx.status(200);
        ctx.json(toResponse(updatedInstructor));
    }

    public void delete(Context ctx) {
        filter.authorize(ctx, Role.INSTRUCTOR);

        int id = getId(ctx);
        Instructor instructor = instructorDAO.getById(id);

        if (instructor == null) {
            ctx.status(404);
            ctx.json(new ErrorResponse(404, "Instructor not found"));
            return;
        }

        instructorDAO.delete(id);
        ctx.status(204);
    }

    public void login(Context ctx) {
        LoginRequestDTO request = ctx.bodyAsClass(LoginRequestDTO.class);

        Instructor instructor = instructorDAO.getByEmail(request.email());

        if (instructor == null || instructor.getPassword() == null ||
                request.password() == null ||
                !BCrypt.checkpw(request.password(), instructor.getPassword())) {

            ctx.status(401);
            ctx.json(new ErrorResponse(401, "Invalid email or password"));
            return;
        }

        String token = JWTToken.generateToken(instructor.getId(), instructor.getRole());

        ctx.status(200);
        ctx.json(Map.of("message", "Login successful", "token", token));
    }

    private int getId(Context ctx) {
        return ctx.pathParamAsClass("id", Integer.class)
                .check(id -> id > 0, "Id must be greater than 0")
                .get();
    }

    private InstructorRequestDTO getValidatedRequest(Context ctx) {
        return ctx.bodyValidator(InstructorRequestDTO.class)
                .check(request -> request.name() != null && !request.name().isBlank(), "Name is required")
                .check(request -> request.email() != null && !request.email().isBlank(), "Email is required")
                .check(request -> request.phoneNumber() != null && !request.phoneNumber().isBlank(), "Phone number is required")
                .check(request -> request.password() != null && !request.password().isBlank(), "Password is required")
                .get();
    }

    private InstructorResponseDTO toResponse(Instructor instructor) {
        return new InstructorResponseDTO(
                instructor.getId(),
                instructor.getName(),
                instructor.getEmail(),
                instructor.getPhoneNumber()
        );
    }
}
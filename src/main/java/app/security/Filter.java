package app.security;

import app.entities.Role;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;

public class Filter {

    public void authenticate(Context ctx) {
        String authHeader = ctx.header("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedResponse("JWT is missing");
        }

        String token = authHeader.substring(7);

        if (!JWTToken.validateToken(token)) {
            throw new UnauthorizedResponse("Invalid JWT");
        }

        DecodedJWT decodedJWT = JWT.decode(token);

        String role = decodedJWT.getClaim("role").asString();
        String subject = decodedJWT.getSubject();

        if (role == null || subject == null) {
            throw new UnauthorizedResponse("JWT is missing user or role");
        }

        try {
            Role.valueOf(role);
            int userId = Integer.parseInt(subject);

            ctx.attribute("userId", userId);
            ctx.attribute("role", role);

        } catch (IllegalArgumentException e) {
            throw new UnauthorizedResponse("Invalid user or role");
        }
    }

    public void authorize(Context ctx, Role requiredRole) {
        String role = ctx.attribute("role");

        if (role == null || !role.equals(requiredRole.name())) {
            throw new ForbiddenResponse("Access denied");
        }
    }

    public void authorizeStudent(Context ctx, int studentId) {
        String role = ctx.attribute("role");
        Integer userId = ctx.attribute("userId");

        if (role == null || userId == null) {
            throw new UnauthorizedResponse("User is not authenticated");
        }

        if (role.equals(Role.INSTRUCTOR.name())) {
            return;
        }

        if (!role.equals(Role.STUDENT.name()) || !userId.equals(studentId)) {
            throw new ForbiddenResponse("You can only access your own information");
        }
    }
}
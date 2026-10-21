package app.security;

import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;

public class Filter {

    public void authenticate(Context ctx) {
        String authHeader = ctx.header("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedResponse("JWT mangler");
        }

        String token = authHeader.substring(7);

        if (!JWTToken.validateToken(token)) {
            throw new UnauthorizedResponse("Ugyldigt JWT");
        }
    }
}
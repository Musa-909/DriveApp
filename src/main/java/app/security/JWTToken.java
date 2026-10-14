package app.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTToken {

    public static String generateToken(int studentId) {

        String secret = System.getenv("JWT_SECRET");

        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT_SECRET mangler");
        }

        return JWT.create()
                .withIssuer("DriveApp")
                .withSubject(String.valueOf(studentId))
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 500000))
                .sign(Algorithm.HMAC256(secret));
    }


    public static boolean validateToken(String token) {

        String secret = System.getenv("JWT_SECRET");

        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT_SECRET mangler");
        }

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer("DriveApp")
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);

            return decodedJWT.getSubject() != null;

        } catch (com.auth0.jwt.exceptions.JWTVerificationException e) {
            return false;
        }
    }

}
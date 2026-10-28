
package app.security;

import app.entities.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JWTTokenTest {
    @Test
    void validJWTTest() {
        String token = JWTToken.generateToken(1, Role.STUDENT);
        assertTrue(JWTToken.validateToken(token));
    }

    @Test
    void validInstructorJWTTest() {
        String token = JWTToken.generateToken(1, Role.INSTRUCTOR);
        assertTrue(JWTToken.validateToken(token));
    }

    @Test
    void invalidJWTTest() {
        String invalidToken = "forkert.token.her";
        assertFalse(JWTToken.validateToken(invalidToken));
    }
}

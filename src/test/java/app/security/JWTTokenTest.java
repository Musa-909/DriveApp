
package app.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JWTTokenTest {

    @Test
    void validJWTTest() {
        String token = JWTToken.generateToken(1);

        assertTrue(JWTToken.validateToken(token));
    }

    @Test
    void invalidJWTTest() {
        String invalidToken = "forkert.token.her";

        assertFalse(JWTToken.validateToken(invalidToken));
    }
}

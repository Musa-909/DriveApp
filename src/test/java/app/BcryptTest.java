package app;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

public class BcryptTest {

    @Test
    void testPasswordHashing() {

        String password = "Test1234";

        String hashedPassword = BCrypt.hashpw(
                password,
                BCrypt.gensalt()
        );

        assertNotEquals(password, hashedPassword);

        assertTrue(BCrypt.checkpw(password, hashedPassword));

        assertFalse(BCrypt.checkpw("Forkert123", hashedPassword));
    }
}

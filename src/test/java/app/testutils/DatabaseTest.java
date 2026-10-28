package app.testutils;

import app.config.HibernateTestConfig;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.postgresql.PostgreSQLContainer;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class DatabaseTest {

    private final PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:16-alpine");

    protected EntityManagerFactory emf;

    @BeforeAll
    void startDatabase() {
        postgres.start();
        emf = HibernateTestConfig.createEntityManagerFactory(postgres);
    }

    @AfterAll
    void closeDatabase() {
        try {
            if (emf != null) {
                emf.close();
            }
        } finally {
            postgres.stop();
        }
    }
}
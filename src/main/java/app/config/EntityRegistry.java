package app.config;

import app.entities.Booking;
import app.entities.Instructor;
import app.entities.Lesson;
import app.entities.Student;
import org.hibernate.cfg.Configuration;
final class EntityRegistry {

    private EntityRegistry() {}

    static void registerEntities(Configuration configuration) {
        configuration.addAnnotatedClass(Student.class);
        configuration.addAnnotatedClass(Instructor.class);
        configuration.addAnnotatedClass(Booking.class);
        configuration.addAnnotatedClass(Lesson.class);
    }
}
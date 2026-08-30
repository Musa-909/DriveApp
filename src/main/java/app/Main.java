package app;

import app.config.HibernateConfig;
import app.dao.StudentDAO;
import app.dao.StudentDAOImpl;
import app.entities.Student;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        System.out.println("Database connected!");

        StudentDAO studentDAO = new StudentDAOImpl();
        Student student = new Student("Musa Sayed", "musa@email.com", "12345678");
        studentDAO.create(student);
        System.out.println(student);

    }
}

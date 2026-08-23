package app.dao;

import app.config.HibernateConfig;
import app.entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    @Override
    public Student create(Student student) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(student);
        em.getTransaction().commit();
        em.close();
        return student;
    }

    @Override
    public Student getById(int id) {
        EntityManager em = emf.createEntityManager();
        Student foundStudent = em.find(Student.class, id);
        em.close();
        return foundStudent;
    }

    @Override
    public List<Student> getAll() {
        EntityManager em = emf.createEntityManager();

        List<Student> students = em
                .createQuery("SELECT s FROM Student s", Student.class)
                .getResultList();

        em.close();
        return students;
    }


    @Override
    public Student update(Student student) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Student updatedStudent = em.merge(student);
        em.getTransaction().commit();
        em.close();
        return updatedStudent;
    }

    @Override
    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Student student = em.find(Student.class, id);

        if (student != null) {
            em.remove(student);
        }

        em.getTransaction().commit();
        em.close();
    }
}
package app.dao;

import app.config.HibernateConfig;
import app.entities.Instructor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class InstructorDAOImpl implements InstructorDAO {

    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    @Override
    public Instructor create(Instructor instructor) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(instructor);
            em.getTransaction().commit();
        }
        return instructor;
    }

    @Override
    public Instructor getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Instructor.class, id);
        }
    }

    @Override
    public Instructor update(Instructor instructor) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Instructor updatedInstructor = em.merge(instructor);
            em.getTransaction().commit();
            return updatedInstructor;
        }
    }

    @Override
    public void delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Instructor instructor = em.find(Instructor.class, id);

            if (instructor != null) {
                em.remove(instructor);
            }

            em.getTransaction().commit();
        }
    }

    @Override
    public List<Instructor> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT i FROM Instructor i", Instructor.class).getResultList();
        }
    }
}
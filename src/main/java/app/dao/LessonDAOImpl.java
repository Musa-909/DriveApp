package app.dao;

import app.config.HibernateConfig;
import app.entities.Lesson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class LessonDAOImpl implements LessonDAO {

    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    @Override
    public Lesson create(Lesson lesson) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(lesson);
            em.getTransaction().commit();
        }
        return lesson;
    }

    @Override
    public Lesson getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Lesson.class, id);
        }
    }

    @Override
    public List<Lesson> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT l FROM Lesson l LEFT JOIN FETCH l.instructor", Lesson.class)
                    .getResultList();
        }
    }

    @Override
    public Lesson update(Lesson lesson) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Lesson updatedLesson = em.merge(lesson);
            em.getTransaction().commit();
            return updatedLesson;
        }
    }

    @Override
    public void delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Lesson lesson = em.find(Lesson.class, id);

            if (lesson != null) {
                em.remove(lesson);
            }

            em.getTransaction().commit();
        }
    }
}
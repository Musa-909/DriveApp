package app.dao;

import app.config.HibernateConfig;
import app.entities.Booking;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class BookingDAOImpl implements BookingDAO {

    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    @Override
    public Booking create(Booking booking) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(booking);
            em.getTransaction().commit();
        }
        return booking;
    }

    @Override
    public Booking getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Booking.class, id);
        }
    }

    @Override
    public List<Booking> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                    "SELECT b FROM Booking b",
                    Booking.class
            ).getResultList();
        }
    }

    @Override
    public Booking update(Booking booking) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Booking updatedBooking = em.merge(booking);
            em.getTransaction().commit();
            return updatedBooking;
        }
    }

    @Override
    public void delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Booking booking = em.find(Booking.class, id);

            if (booking != null) {
                em.remove(booking);
            }

            em.getTransaction().commit();
        }
    }

    @Override
    public boolean existsByLessonId(int lessonId) {
        try (EntityManager em = emf.createEntityManager()) {

            Long count = em.createQuery(
                            "SELECT COUNT(b) FROM Booking b WHERE b.lesson.id = :lessonId",
                            Long.class)
                    .setParameter("lessonId", lessonId)
                    .getSingleResult();

            return count > 0;
        }
    }
}
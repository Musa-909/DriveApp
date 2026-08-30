package app.dao;

import app.config.HibernateConfig;
import app.entities.Booking;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

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
}
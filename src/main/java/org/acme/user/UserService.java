package org.acme.user;

import java.util.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Transactional(Transactional.TxType.SUPPORTS)
@ApplicationScoped
public class UserService {
    @Inject
    EntityManager em;
    UUID uuid;

    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public User find(Long id) {
        return em.find(User.class, id);
    }

    public Long countUsers() {
        return em.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public User createUser(User user) {
        user.setUserUUID(UUID.randomUUID());
        em.persist(user);
        return user;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteUser(Long id) {
        em.remove(em.getReference(User.class, id));
    }

    public User getUserUUID(UUID userUUID) {
        return em.find(User.class, userUUID);
    }
}

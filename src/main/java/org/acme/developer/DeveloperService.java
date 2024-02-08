package org.acme.developer;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;


@Transactional(Transactional.TxType.SUPPORTS)
@ApplicationScoped
public class DeveloperService {
    
    @Inject
    EntityManager em;
    UUID uuid;

    public List<Developer> findAll() {
    List<Developer> developers = em.createQuery("SELECT d FROM Developer d", Developer.class).getResultList();
    return developers;
    }

    public Developer find(Long id) {
        return em.find(Developer.class, id);
    } 

    public Long countDevelopers() {
        return em.createQuery("SELECT COUNT(d) FROM Developer d", Long.class).getSingleResult(); 
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Developer createDeveloper(Developer developer) {
        developer.setApiKey(UUID.randomUUID());
        em.persist(developer);
        return developer;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteDeveloper(Long id) {
         em.remove(em.getReference(Developer.class, id));
    } 
}

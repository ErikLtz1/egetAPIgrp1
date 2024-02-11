package org.acme.developer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
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

    public Developer findDeveloperByEmail(String email) {
        return em.createQuery("SELECT d FROM Developer d WHERE d.email LIKE :email", Developer.class)
        .setParameter("email", email).getSingleResult();
    }

    public Long countDevelopers() {
        return em.createQuery("SELECT COUNT(d) FROM Developer d", Long.class).getSingleResult(); 
    }

    public String deleteDeveloper(String email, UUID apiKey, String password) {
        Developer developer = em.createQuery("SELECT d FROM Developer d WHERE d.email LIKE :email", Developer.class)
        .setParameter("email", email).getSingleResult();

        String salt = developer.getSalt();
        String securePassword = getSecurePassword(password, salt);
        
        String message;

        if(email.equals(developer.getEmail()) && apiKey.equals(developer.getApiKey()) && securePassword.equals(developer.getPassword())) {
            deleteDeveloper(developer.getId());
            message = "Developer raderad!";
        } else {
            message = "Developer inte raderad! Något matchar inte!";
        }

        return message;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Developer createDeveloper(Developer developer) throws NoSuchAlgorithmException, NoSuchProviderException {
        developer.setApiKey(UUID.randomUUID());
        String salt = getSalt();
        String securePassword = getSecurePassword(developer.getPassword(), salt);
        developer.setPassword(securePassword);
        developer.setSalt(salt);
        em.persist(developer);
        return developer;
    }

    public boolean getDevelopersApiKey(UUID apiKey) {

        List<Developer> developers = em.createQuery("SELECT d FROM Developer d", Developer.class).getResultList();
        for (Developer developer: developers)  {
            if (developer.getApiKey().equals(apiKey)) {
                return true;
             }
        }
        
        return false;
        
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteDeveloper(Long id) {
         em.remove(em.getReference(Developer.class, id));
    } 

    private static String getSalt()
            throws NoSuchAlgorithmException, NoSuchProviderException 
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");

        byte[] salt = new byte[16];

        sr.nextBytes(salt);
        
        return salt.toString();
    }

    
    private static String getSecurePassword(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(salt.getBytes());

            byte[] bytes = md.digest(passwordToHash.getBytes());

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }

            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

   

}

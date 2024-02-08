package org.acme.developer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Base64;
// import jakarta.security.enterprise.identitystore.*;
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

    public Developer findEmail(String email) {
        return em.find(Developer.class, email);
    }

    public Long countDevelopers() {
        return em.createQuery("SELECT COUNT(d) FROM Developer d", Long.class).getSingleResult(); 
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Developer createDeveloper(Developer developer) throws NoSuchAlgorithmException, NoSuchProviderException {
        developer.setApiKey(UUID.randomUUID());
        String salt = getSalt();
        String securePassword = getSecurePassword(developer.getPassword(), salt);
        developer.setPassword(securePassword);
        em.persist(developer);
        return developer;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteDeveloper(Long id) {
         em.remove(em.getReference(Developer.class, id));
    } 

    private static String getSalt()
            throws NoSuchAlgorithmException, NoSuchProviderException 
    {
        // Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");

        // Create array for salt
        byte[] salt = new byte[16];

        // Get a random salt
        sr.nextBytes(salt);

        // return salt
        return salt.toString();
    }

    
    private static String getSecurePassword(String passwordToHash,

            String salt) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Add password bytes to digest
            md.update(salt.getBytes());

            // Get the hash's bytes
            byte[] bytes = md.digest(passwordToHash.getBytes());

            // This bytes[] has bytes in decimal format;
            // Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }

            // Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

   

}

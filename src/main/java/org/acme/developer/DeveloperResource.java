package org.acme.developer;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.acme.post.Post;
import org.acme.post.PostService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/developer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class DeveloperResource {

    @Inject
    DeveloperService developerService;

    @GET
    public Response getDevelopers() {

        List<Developer> developers = developerService.findAll();
        if (developers.isEmpty()) {
            
            return Response.noContent().build();
        }

        return Response.ok(developers).build();
        
    }

    @GET
    @Path("/{id}")
    public Response getDevelopersById(@PathParam("id") Long id) {

        Developer developer = developerService.find(id);

        return Response.ok(developer).build();

    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/count")
    public Response countDevelopers() {

        Long count = developerService.countDevelopers();
        return Response.ok(count).build();
    }

    @POST //När vi anropar post endpoints så tar vi emot ett paket som vi skickar i våran post och det innehåller det vi specat
    public Response createDeveloper(@Valid Developer developer) throws URISyntaxException, NoSuchAlgorithmException, NoSuchProviderException { //Felhanterar

        //ger id till robot
        developer = developerService.createDeveloper(developer);

        URI createdUri = new URI(developer.getId().toString()); //Addressen till resursen
        return Response.created(createdUri).entity(developer).build(); //Skickar tbx det objektet vi har skapat
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDeveloper(@PathParam("id") Long id) {

        developerService.deleteDeveloper(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/login")
    public String loginDeveloper(@Valid Developer developer) throws NoSuchAlgorithmException, NoSuchProviderException {
        String salt = getSalt();
        String securePassword = getSecurePassword(developer.getPassword(), salt);
        String message;
        if (developerService.findEmail(developer.getEmail()).getPassword()== securePassword) {
            message = "Det funkar";
        }
        else {
            message = "Funkar inte";
        }
        return message;
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

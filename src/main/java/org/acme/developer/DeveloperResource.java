package org.acme.developer;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;


import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
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

    //denna admin är endast för utvecklingssyfte för oss
    Admin admin = new Admin("admin", "123");

    @Inject
    DeveloperService developerService;

    @POST
    @Path ("/developers")
    public Response getDevelopers(@Valid Admin loginAdmin) {

        if (loginAdmin.getUsername().equals(admin.getUsername()) && loginAdmin.getPassword().equals(admin.getPassword())) {
            List<Developer> developers = developerService.findAll();

            if (developers.isEmpty()) {
                return Response.noContent().build();
            }

            return Response.ok(developers).build();

        } else {

            return Response.status(403).build();

        }
    
        
    }

    @POST
    @Path("/{id}")
    public Response getDevelopersById(@PathParam("id") Long id, @Valid Admin loginAdmin) {

        if (loginAdmin.getUsername().equals(admin.getUsername()) && loginAdmin.getPassword().equals(admin.getPassword())) {
            return Response.ok(developerService.find(id)).build();

        } else {

            return Response.status(403).build();
            
        }

    

    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/count")
    public Response countDevelopers(@Valid Admin loginAdmin) {
        if (loginAdmin.getUsername().equals(admin.getUsername()) && loginAdmin.getPassword().equals(admin.getPassword())) {
    
            return Response.ok(developerService.countDevelopers()).build();

        } else {

            return Response.status(403).build();
            
        }

        
    }

    @POST //När vi anropar post endpoints så tar vi emot ett paket som vi skickar i våran post och det innehåller det vi specat
    public Response createDeveloper(@Valid Developer developer) throws URISyntaxException, NoSuchAlgorithmException, NoSuchProviderException { //Felhanterar

        developer = developerService.createDeveloper(developer);

        URI createdUri = new URI(developer.getId().toString()); //Addressen till resursen
        return Response.created(createdUri).entity(developer).build(); //Skickar tbx det objektet vi har skapat
    }

    @DELETE
    @Path("/delete")
    public String deleteDeveloper(@Valid Developer developer) throws NoSuchAlgorithmException, NoSuchProviderException {
        
        return developerService.deleteDeveloper(developer.getEmail(), developer.getApiKey(), developer.getPassword());
    }


}

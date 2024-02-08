package org.acme.developer;

import java.net.URI;
import java.net.URISyntaxException;
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
    public Response createDeveloper(@Valid Developer developer) throws URISyntaxException { //Felhanterar

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
}

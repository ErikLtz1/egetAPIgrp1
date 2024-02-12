package org.acme.user;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.acme.developer.DeveloperService;
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

@Path("/api/{apikey}/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class UserResource {

    @Inject
    UserService userService;
    @Inject
    DeveloperService developerService;

    @GET
    public Response getUsers(@PathParam("apikey") UUID apikey) {

        if (developerService.getDevelopersApiKey(apikey)) {
            List<User> users = userService.findAll();
            return Response.ok(users).build();
        } else {
            return Response.status(403).build();
        } 
    }

    @GET
    @Path("/{id}")
    public Response getUsersById(@PathParam("id") Long id, @PathParam("apikey") UUID apikey) {

        if (developerService.getDevelopersApiKey(apikey)) {
            User user = userService.find(id);
            return Response.ok(user).build();
        } else {
            return Response.status(403).build();
        }
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/count")
    public Response countUsers(@PathParam("apikey") UUID apikey) {

        if (developerService.getDevelopersApiKey(apikey)) {
            Long count = userService.countUsers();
            return Response.ok(count).build();
        } else {
            return Response.status(403).build();
        }    
    }

    @POST // När vi anropar post endpoints så tar vi emot ett paket som vi skickar i våran
    @Path("/")      // post och det innehåller det vi specat
    public Response createUser(@Valid User user, @PathParam("apikey") UUID apikey) throws URISyntaxException { // Felhanterar

        if (developerService.getDevelopersApiKey(apikey)) {
            user = userService.createUser(user);
    
            URI createdUri = new URI(user.getId().toString()); // Addressen till resursen
            return Response.created(createdUri).entity(user).build(); // Skickar tbx det objektet vi har skapat
        } 
        return Response.status(403).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id, @PathParam("apikey") UUID apikey) {
        if (developerService.getDevelopersApiKey(apikey)) {
            userService.deleteUser(id);
            return Response.ok(id).build();
        } else {
            return Response.status(403).build();
        } 
    }
}

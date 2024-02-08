package org.acme.user;

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

@Path("/api/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class UserResource {

    @Inject
    UserService userService;

    @GET
    public Response getUsers() {

        List<User> users = userService.findAll();
        if (users.isEmpty()) {

            return Response.noContent().build();
        }

        return Response.ok(users).build();

    }

    @GET
    @Path("/{id}")
    public Response getUsersById(@PathParam("id") Long id) {

        User user = userService.find(id);

        return Response.ok(user).build();

    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/count")
    public Response countUsers() {

        Long count = userService.countUsers();
        return Response.ok(count).build();
    }

    @POST // När vi anropar post endpoints så tar vi emot ett paket som vi skickar i våran
          // post och det innehåller det vi specat
    public Response createUser(@Valid User user) throws URISyntaxException { // Felhanterar

        // ger id till robot
        user = userService.createUser(user);

        URI createdUri = new URI(user.getId().toString()); // Addressen till resursen
        return Response.created(createdUri).entity(user).build(); // Skickar tbx det objektet vi har skapat
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {

        // robotService.delete(id);
        return Response.noContent().build();
    }
}

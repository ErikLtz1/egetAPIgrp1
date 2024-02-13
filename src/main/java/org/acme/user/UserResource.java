package org.acme.user;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.acme.developer.DeveloperService;
import org.acme.post.Post;
import org.acme.post.PostService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

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
    @Operation(summary ="Hämtning av användare", description = "Utvecklare kan hämta alla användare ifrån databasen")
    @APIResponse(
        responseCode = "200",
        description = "Lyckad hämtning av användare" 
    )
    @APIResponse(
        responseCode = "400",
        description = "Användarnamn eller lösenord saknas" 
    )
    @APIResponse(
        responseCode = "500",
        description = "Något felaktigt i URL" 
    )
    public Response getUsers(@PathParam("apikey") UUID apikey) {

        try {

            if (developerService.getDevelopersApiKey(apikey)) {
                List<User> users = userService.findAll();
                return Response.ok(users).build();
            } else {
                return Response.status(403).build();
            } 
        } catch (Exception e) {
            return Response.status(500).entity("Ett fel uppstod vid redigering av inlägget").build();
        }
    }

    @GET
    @Operation(summary ="Hämta specifik användare", description = "Utvecklare kan hämta en specifik user från databasen")
    @APIResponse(
        responseCode = "200",
        description = "Lyckad hämtning av användare" 
    )
    @APIResponse(
        responseCode = "400",
        description = "Användarnamn eller lösenord saknas" 
    )
    @APIResponse(
        responseCode = "500",
        description = "Något felaktigt i URL" 
    )
    @Path("/{id}")
    public Response getUsersById(@PathParam("id") Long id, @PathParam("apikey") UUID apikey) {

        try {

            if (developerService.getDevelopersApiKey(apikey)) {
                User user = userService.find(id);
                return Response.ok(user).build();
            } else {
                return Response.status(403).build();
            }
        } catch (Exception e) {
            return Response.status(500).entity("Ett fel uppstod vid redigering av inlägget").build();
        }
    }

    @GET
    @Operation(summary ="Hämta antal användare", description = "Utvecklare kan hämta antalet användare från databasen")
    @APIResponse(
        responseCode = "200",
        description = "Lyckad hämtning av antal användare" 
    )
    @APIResponse(
        responseCode = "403",
        description = "Behörighet saknas" 
    )
    @APIResponse(
        responseCode = "500",
        description = "Något felaktigt i URL" 
    )
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/count")
    public Response countUsers(@PathParam("apikey") UUID apikey) {
        try {

            if (developerService.getDevelopersApiKey(apikey)) {
                Long count = userService.countUsers();
                return Response.ok(count).build();
            } else {
                return Response.status(403).build();
            }    
        } catch (Exception e) {
            return Response.status(500).entity("Ett fel uppstod vid redigering av inlägget").build();
        }
    }

    @POST // När vi anropar post endpoints så tar vi emot ett paket som vi skickar i våran
    @Operation(summary ="Registrering av användare", description = "Användare skapar konto för att få mer behörighet på sidan")
    @APIResponse(
        responseCode = "201",
        description = "Lyckad registrering" 
    )
    @APIResponse(
        responseCode = "400",
        description = "Användarnamn eller lösenord saknas" 
    )
    @APIResponse(
        responseCode = "500",
        description = "Något felaktigt i URL" 
    )
    @Path("/")      // post och det innehåller det vi specat
    public Response createUser(@Valid User user, @PathParam("apikey") UUID apikey) throws URISyntaxException { // Felhanterar

      try {
        if (developerService.getDevelopersApiKey(apikey)) {
            user = userService.createUser(user);
    
            URI createdUri = new URI(user.getId().toString()); // Addressen till resursen
            return Response.created(createdUri).entity(user).build(); // Skickar tbx det objektet vi har skapat
        } 
        return Response.status(403).build();
    } catch (Exception e) {
        return Response.status(500).entity("Ett fel uppstod vid redigering av inlägget").build();
    }
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

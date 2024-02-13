package org.acme.developer;

import java.net.*;
import java.security.*;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("/api/developer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeveloperResource {
    @Inject
    DeveloperService developerService;
    Admin admin = new Admin("admin", "123"); // denna admin är endast för utvecklingssyfte för oss

    @POST
    @Operation(summary = "Admin kan visa alla utvecklare", description = "Admin kan hämta och visa samtliga utvecklare i databasen")
    @APIResponse(responseCode = "200", description = "Alla utvecklare")
    @APIResponse(responseCode = "403", description = "Ingen behörighet")
    @APIResponse(responseCode = "500", description = "Något felaktigt i URL")
    @Path("/developers")
    public Response getDevelopers(@Valid Admin loginAdmin) {
        try {
            if (loginAdmin.getUsername().equals(admin.getUsername())
                    && loginAdmin.getPassword().equals(admin.getPassword())) {
                List<Developer> developers = developerService.findAll();
                return developers.isEmpty() ? Response.noContent().build() : Response.ok(developers).build();
            } else {
                return Response.status(403).build();
            }
        } catch (Exception e) {
            return Response.status(500).entity("Ett fel uppstod vid redigering av inlägget").build();
        }
    }

    @POST
    @Operation(summary = "Admin kan visa specifik utvecklare", description = "Admin kan hämta och visa specifika utvecklare i databasen")
    @APIResponse(responseCode = "200", description = "Visar specifik utvecklare")
    @APIResponse(responseCode = "403", description = "Ingen behörighet")
    @APIResponse(responseCode = "500", description = "Något felaktigt i URL")
    @Path("/{id}")
    public Response getDevelopersById(@PathParam("id") Long id, @Valid Admin loginAdmin) {
        try {
            return loginAdmin.getUsername().equals(admin.getUsername())
                    && loginAdmin.getPassword().equals(admin.getPassword())
                            ? Response.ok(developerService.find(id)).build()
                            : Response.status(403).build();
        } catch (Exception e) {
            return Response.status(500).entity("Ett fel uppstod vid redigering av inlägget").build();
        }
    }

    @POST
    @Operation(summary = "Admin kan se antal utvecklare", description = "Admin kan hämta och visa antal utvecklare i databasen")
    @APIResponse(responseCode = "200", description = "Visar antal utvecklare")
    @APIResponse(responseCode = "403", description = "Ingen behörighet")
    @APIResponse(responseCode = "500", description = "Något felaktigt i URL")
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/count")
    public Response countDevelopers(@Valid Admin loginAdmin) {
        try {
            return loginAdmin.getUsername().equals(admin.getUsername())
                    && loginAdmin.getPassword().equals(admin.getPassword())
                            ? Response.ok(developerService.countDevelopers()).build()
                            : Response.status(403).build();
        } catch (Exception e) {
            return Response.status(500).entity("Ett fel uppstod vid redigering av inlägget").build();
        }
    }

    // När vi anropar post endpoints så tar vi emot ett paket som vi skickar i våran
    // post och det innehåller det vi specat
    @POST
    @Operation(summary = "Registrering för API-nyckel", description = "Utvecklare skapar inlogg för att få tillgång till API-nyckel")
    @APIResponse(responseCode = "201", description = "Lyckad registrering")
    @APIResponse(responseCode = "400", description = "Email eller lösenord saknas")
    @APIResponse(responseCode = "500", description = "Något felaktigt i URL")
    public Response createDeveloper(@Valid Developer developer)
            throws URISyntaxException, NoSuchAlgorithmException, NoSuchProviderException { // Felhanterar
        try {
            developer = developerService.createDeveloper(developer);
            URI createdUri = new URI(developer.getId().toString()); // Addressen till resursen
            return Response.created(createdUri).entity(developer).build(); // Skickar tbx det objektet vi har skapat
        } catch (Exception e) {
            return Response.status(500).entity("Ett fel uppstod vid redigering av inlägget").build();
        }
    }

    @DELETE
    @Operation(summary = "Registrering för API-nyckel", description = "Utvecklare skapar inlogg för att få tillgång till API-nyckel")
    @APIResponse(responseCode = "201", description = "Lyckad registrering")
    @APIResponse(responseCode = "400", description = "Email eller lösenord saknas")
    @Path("/delete")
    public String deleteDeveloper(@Valid Developer developer) throws NoSuchAlgorithmException, NoSuchProviderException {
        return developerService.deleteDeveloper(developer.getEmail(), developer.getApiKey(), developer.getPassword());
    }
}

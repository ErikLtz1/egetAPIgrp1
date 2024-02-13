package org.acme.post;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.acme.developer.DeveloperService;
import org.acme.user.UserService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/{apikey}/post")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class PostResource {

    @Inject
    PostService postService;
    @Inject
    DeveloperService developerService;
    @Inject
    UserService userService;

    @GET
    @Operation(summary ="Visar alla inlägg", description = "Användare som har ett konto kan visa alla inlägg som finns")
    @APIResponse(
        responseCode = "201",
        description = "Lyckad hämtning av alla inlägg" 
    )
    @APIResponse(
        responseCode = "403",
        description = "Åtkomst nekad" 
    )
    @APIResponse(
        responseCode = "500",
        description = "Något felaktigt i URL" 
    )
    public Response getPosts(@PathParam("apikey") UUID apikey) {
       try {
        if (developerService.getDevelopersApiKey(apikey)) {
            List<Post> posts = postService.findAll();

            if (posts.isEmpty()) {
                return Response.noContent().build();
            }
            return Response.ok(posts).build();
        } else {
            return Response.status(403).build();
        }
       } catch (Exception e) {
        return Response.status(500).entity("Ett fel uppstod vid redigering av inlägget").build();
       }
    }

    @GET
    @Operation(summary ="Visar specifikt inlägg", description = "Visar specifikt inlägg")
    @APIResponse(
        responseCode = "200",
        description = "Lyckad hämtning av alla inlägg" 
    )
    @APIResponse(
        responseCode = "403",
        description = "Åtkomst nekad" 
    )
    @APIResponse(
        responseCode = "404",
        description = "Det unika ID finns inte." 
    )
    @APIResponse(
        responseCode = "500",
        description = "Något felaktigt i URL" 
    )
    @Path("/{id}")
    public Response getPostsById(@PathParam("id") Long id, @PathParam("apikey") UUID apikey) {
        try {
            if (developerService.getDevelopersApiKey(apikey)) {
                Post post = postService.find(id);
                if (post != null) {
    
                    return Response.ok(post).build();
                } else {
                    return Response.status(404).build();
                }
            } else {
                 return Response.status(403).build();
            }
        } catch (Exception e) {
            return Response.status(500).entity("Ett fel uppstod vid redigering av inlägget").build();
        }
    }

    @GET
    @Operation(summary ="Visar antal inlägg", description = "Visar antalet inlägg i databasen")
    @APIResponse(
        responseCode = "200",
        description = "Lyckad hämtning av antal inlägg" 
    )
    @APIResponse(
        responseCode = "403",
        description = "Åtkomst nekad" 
    )
    @APIResponse(
        responseCode = "404",
        description = "Det unika ID finns inte." 
    )
    @APIResponse(
        responseCode = "500",
        description = "Något felaktigt i URL" 
    )
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/count")
    public Response countPosts(@PathParam("apikey") UUID apikey) {
        try {
            if (developerService.getDevelopersApiKey(apikey)) {
                Long count = postService.countPosts();
                return Response.ok(count).build();
            } else {
                return Response.status(403).build();
            }
        } catch (Exception e) {
            return Response.status(500).entity("Ett fel uppstod vid redigering av inlägget").build();
        }
    }

    @POST
    @Operation(summary ="Skapande av inlägg", description = "Användare som har ett konto kan skapa ett inlägg")
    @APIResponse(
        responseCode = "201",
        description = "Lyckat skapande av inlägg" 
    )
    @APIResponse(
        responseCode = "400",
        description = "Priset finns ej eller är för lågt eller saknas ett namn" 
    )
    @APIResponse(
        responseCode = "403",
        description = "Åtkomst nekad" 
    )
    @APIResponse(
        responseCode = "404",
        description = "Felaktig URL" 
    )
    @APIResponse(
        responseCode = "500",
        description = "Något felaktigt i URL" 
    )

    
    @Path("/{userUUID}")
    public Response createPost(@Valid Post post, @PathParam("apikey") UUID apikey, @PathParam("userUUID") UUID userUUID) throws URISyntaxException {
        
        try {
            if (developerService.getDevelopersApiKey(apikey)) {
                post = postService.createPost(post, userUUID);
                // Tilldela användarens UUID till posten
                post.setPostUserUUID(userUUID);
                URI createdUri = new URI(post.getId().toString()); // Addressen till resursen
                return Response.created(createdUri).entity(post).build(); // Skickar tbx det objektet vi har skapat
            } else {
                return Response.status(403).build();
            }
        } catch(Exception e) {
            return Response.status(500).entity("Ett fel uppstod vid redigering av inlägget").build();
        }
    }
   

    @DELETE
    @Operation(summary ="Radera specifikt inlägg", description = "Användare som har skapat ett inlägg kan radera det")
    @APIResponse(
        responseCode = "200",
        description = "Raderat inlägg lyckat" 
    )
    @APIResponse(
        responseCode = "403",
        description = "Åtkomst nekad" 
    )
    @APIResponse(
        responseCode = "404",
        description = "Felaktig URL" 
    )
    @APIResponse(
        responseCode = "500",
        description = "Något felaktigt i URL" 
    )
    @Path("/{userUUID}/{id}")
    public Response deletePost(@PathParam("id") @Min(1) Long id, @PathParam("apikey") UUID apikey,
            @PathParam("userUUID") UUID userUUID) {
        try {
            if (developerService.getDevelopersApiKey(apikey)) {
                Post post = postService.find(id);
                if (post != null) {
                    postService.deletePost(id, userUUID);
    
                    return Response.ok().build();
                } else {
                    return Response.status(404).build();
                }
    
                
            } else {
                return Response.status(403).build();
            }
        } catch (Exception e){
            return Response.status(500).entity("Ett fel uppstod vid redigering av inlägget").build();
        }
        
    }

    @PATCH
    @Operation(summary ="Ändra specifikt inlägg", description = "Användare som har skapat ett inlägg kan ändra det")
    @APIResponse(
        responseCode = "200",
        description = "Ändring av inlägg lyckat" 
    )
    @APIResponse(
        responseCode = "403",
        description = "Åtkomst nekad" 
    )
    @APIResponse(
        responseCode = "404",
        description = "Angivet ID för inlägget finns ej" 
    )
    @APIResponse(
        responseCode = "500",
        description = "Något felaktigt i URL" 
    )
    @Path("/{userUUID}/edit/{id}")
    public Response editPost(@Valid Post post, @PathParam("id") Long id, @PathParam("apikey") UUID apikey, 
    @PathParam("userUUID") UUID userUUID) {
     try {
        if (developerService.getDevelopersApiKey(apikey)) {
             
            if (postService.find(id) != null) {
                
                postService.editPost(userUUID, id, post);

                return Response.ok(post).build();
            } else {
                return Response.status(404).build();
            }
            
        } else {
            return Response.status(403).build();
        }
     } catch (Exception e) {
        
        return Response.status(500).entity("Ett fel uppstod vid redigering av inlägget").build();
    }
    }
}
    


package org.acme.post;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.acme.developer.DeveloperService;

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

@Path("/api/{apikey}/post")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class PostResource {

    @Inject
    PostService postService;
    @Inject
    DeveloperService developerService;

    @GET
    
    public Response getPosts(@PathParam("apikey") UUID apikey) {
        if (developerService.getDevelopersApiKey(apikey)) {
            List<Post> posts = postService.findAll();

            if (posts.isEmpty()) {
                return Response.noContent().build();
            }
            return Response.ok(posts).build();
        } else {
            return Response.status(403).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getPostsById(@PathParam("id") Long id, @PathParam("apikey") UUID apikey) {
        if (developerService.getDevelopersApiKey(apikey)) {
            Post post = postService.find(id);
            return Response.ok(post).build();
        } else {
             return Response.status(403).build();
        }
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/count")
    public Response countPosts(@PathParam("apikey") UUID apikey) {
        if (developerService.getDevelopersApiKey(apikey)) {
            Long count = postService.countPosts();
            return Response.ok(count).build();
        } else {
            return Response.status(403).build();
        }
    }

    @POST //När vi anropar post endpoints så tar vi emot ett paket som vi skickar i våran post och det innehåller det vi specat
    public Response createPost(@Valid Post post, @PathParam("apikey") UUID apikey) throws URISyntaxException { //Felhanterar
        if (developerService.getDevelopersApiKey(apikey)) {
            post = postService.createPost(post);
            URI createdUri = new URI(post.getId().toString()); //Addressen till resursen
            return Response.created(createdUri).entity(post).build(); //Skickar tbx det objektet vi har skapat
        } else {
            return Response.status(403).build();
        }  
    }

    @DELETE
    @Path("/{id}")
    public Response deletePost(@PathParam("id") @Min(1) Long id, @PathParam ("apikey") UUID apikey) {
        if (developerService.getDevelopersApiKey(apikey)) {
            postService.deletePost(id);
            return Response.ok(id).build();
        } else {
            return Response.status(403).build();
        }
        //TODO felhantering 500
    }
}


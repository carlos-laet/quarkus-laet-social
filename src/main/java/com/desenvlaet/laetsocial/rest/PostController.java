package com.desenvlaet.laetsocial.rest;

import com.desenvlaet.laetsocial.model.Post;
import com.desenvlaet.laetsocial.model.User;
import com.desenvlaet.laetsocial.repository.FollowerRepository;
import com.desenvlaet.laetsocial.repository.PostRepository;
import com.desenvlaet.laetsocial.repository.UserRepository;
import com.desenvlaet.laetsocial.rest.dto.CreatePostRequest;
import com.desenvlaet.laetsocial.rest.dto.PostResponse;
import io.quarkus.panache.common.Sort;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostController {

    private UserRepository userRepository;
    private PostRepository repository;
    private FollowerRepository followerRepository;

    @Inject
    public PostController(UserRepository userRepository, PostRepository repository, FollowerRepository followerRepository) {
        this.userRepository = userRepository;
        this.repository = repository;
        this.followerRepository = followerRepository;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest request) {
        User user = userRepository.findById(userId);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Post post = new Post();
        post.setText(request.getText());
        post.setUser(user);

        repository.persist(post);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPosts(@PathParam("userId") Long userId, @HeaderParam("followerId") Long followerId) {
        User user = userRepository.findById(userId);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (followerId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("You forgot the header followerId").build();
        }

        User follower = userRepository.findById(followerId);

        if (follower  == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Inexistent followerId").build();
        }

        boolean followers = followerRepository.followers(follower, user);

        if (!followers) {
            return Response.status(Response.Status.FORBIDDEN).entity("You can't see these posts").build();
        }

        var query = repository.find("user", Sort.by("dateTime", Sort.Direction.Descending), user);
        var list = query.list();

        var postResposeList = list.stream().map(PostResponse::fromEntity).collect(Collectors.toList());

        return Response.ok(postResposeList).build();
    }
}

package com.desenvlaet.laetsocial.rest;

import com.desenvlaet.laetsocial.model.User;
import com.desenvlaet.laetsocial.repository.UserRepository;
import com.desenvlaet.laetsocial.rest.dto.CreateUserRequest;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    private UserRepository repository;

    @Inject
    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest) {
        User user = new User();
        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());

        //TODO USANDO PANACHE ENTITY
        //user.persist();

        //TODO USANDO PANACHE - REPOSITORY
        repository.persist(user);

        return Response.ok(user).build();
    }

    @GET
    public Response listAllUsers() {
        //TODO USANDO PANACHE ENTITY
        //PanacheQuery<User> query = User.findAll();
        //TODO USANDO PANACHE - REPOSITORY
        PanacheQuery<User> query = repository.findAll();
        return Response.ok(query.list()).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id) {
        //TODO USANDO PANACHE ENTITY
        //User user = User.findById(id);
        //TODO USANDO PANACHE - REPOSITORY
        User user = repository.findById(id);
        if (user != null) {
            repository.delete(user);
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, CreateUserRequest userData) {

        //TODO USANDO PANACHE ENTITY
        //User user = User.findById(id);
        //TODO USANDO PANACHE - REPOSITORY
        User user = repository.findById(id);

        if (user != null) {
            user.setName(userData.getName());
            user.setAge(userData.getAge());
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
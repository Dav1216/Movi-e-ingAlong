package com.movingalong.restservices;

import java.time.Instant;
import java.util.Optional;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.Data;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import com.movingalong.dao.UserDAO;
import com.movingalong.entities.User;

/**
 * RESTful service for managing User entities.
 */
@Path("/user")
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    private UserDAO userDao = new UserDAO();

    /**
     * Adds a new User to the database.
     *
     * @param userBean The UserBean object containing data for the new User.
     * @return a Response indicating the result of the operation.
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response addUser(@BeanParam UserBean userBean) {
        try {
            User user = new User(
                    userBean.getUsername(),
                    userBean.getDateBirth(),
                    Instant.now().toString(),
                    userBean.getEmail(),
                    userBean.getPassword(),
                    userBean.getPhotoURL());

            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashedPassword);

            userDao.addUser(user);
            return Response.status(Response.Status.CREATED).entity("User added successfully").build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error adding user", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error adding user").build();
        }
    }

    /**
     * Updates an existing User in the database.
     */
    @POST
    @Path("/update/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response updateUser(@PathParam("id") String id, @BeanParam UserBean userBean) {
        try {
            if (!ObjectId.isValid(id)) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID format").build();
            }
            ObjectId objectId = new ObjectId(id);

            User user = new User(
                    userBean.getUsername(),
                    userBean.getDateBirth(),
                    Instant.now().toString(),
                    userBean.getEmail(),
                    userBean.getPassword(),
                    userBean.getPhotoURL());

            boolean isUpdated = userDao.updateUser(objectId, user);
            if (isUpdated) {
                return Response.status(Response.Status.OK).entity("User updated successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found with provided ID").build();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating user", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating user").build();
        }
    }

    /**
     * Retrieves a specific User item by its ID.
     */
    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("User ID is required").build();
            }
            Optional<User> userOptional = userDao.getUser(new ObjectId(id));

            if (userOptional.isPresent()) {
                return Response.ok(userOptional.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving user", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving user").build();
        }

    }

    /**
     * Deletes a specific User from the database.
     */
    @POST
    @Path("/delete/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response deleteUser(@PathParam("id") String id) {
        try {
            if (!ObjectId.isValid(id)) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID format").build();
            }
            ObjectId objectId = new ObjectId(id);

            boolean isDeleted = userDao.deleteUser(objectId);
            if (isDeleted) {
                return Response.status(Response.Status.OK).entity("User deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found with provided ID").build();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting user", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting user").build();
        }
    }

    /**
     * Retrieves a specific User from the database based on their username.
     *
     * @param username The username of the User to retrieve.
     * @return a Response containing the User data if found, or an appropriate error
     *         response
     *         if the username is invalid, or the User is not found.
     */
    @GET
    @Path("/getByUsername/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByUsername(@PathParam("username") String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Username is required").build();
            }

            Optional<User> userOptional = userDao.getUserByUsername(username);

            if (userOptional.isPresent()) {
                return Response.ok(userOptional.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving user", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving user").build();
        }
    }

    /**
     * Inner class representing a data transfer object for User data.
     */
    @Data
    public static class UserBean {
        @FormParam("username")
        private String username;

        @FormParam("dateBirth")
        private String dateBirth;

        @FormParam("email")
        private String email;

        @FormParam("password")
        private String password;

        @FormParam("photoURL")
        private String photoURL;
    }
}

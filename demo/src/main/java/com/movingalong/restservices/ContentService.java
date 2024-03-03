package com.movingalong.restservices;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

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

import com.movingalong.dao.ContentDAO;

import com.movingalong.entities.Content;
import com.movingalong.helpers.DateParser;

/**
 * RESTful service for managing Content entities.
 */
@Path("/content")
public class ContentService {

    private static final Logger logger = Logger.getLogger(ContentService.class.getName());
    private ContentDAO contentDao = new ContentDAO();

    /**
     * Adds a new Content item to the database.
     *
     * @param contentBean The ContentBean object containing data for the new
     *                    content.
     * @return a Response indicating the result of the operation.
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response addContent(@BeanParam ContentBean contentBean) {
        try {
            LocalDate date = DateParser.parseDateString(contentBean.getReleaseDate());

            Content content = new Content(
                    contentBean.getTitle(),
                    contentBean.getGenre(),
                    contentBean.getDescription(),
                    contentBean.getStatus(),
                    date,
                    contentBean.getPhotoURL(),
                    contentBean.getAgeRating(),
                    Integer.parseInt(contentBean.getScoreRating()),
                    contentBean.getType());
            contentDao.addContent(content);

            return Response.status(Response.Status.CREATED).entity("Content added successfully").build();
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Invalid score rating", e);
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid score rating").build();
        }
    }

    /**
     * Updates an existing Content item in the database.
     *
     * @param id          The ID of the Content to update.
     * @param contentBean The ContentBean object containing updated data for the
     *                    content.
     * @return a Response indicating the result of the operation.
     */
    @POST
    @Path("/update/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response updateContent(@PathParam("id") String id, @BeanParam ContentBean contentBean) {
        try {
            if (!ObjectId.isValid(id)) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID format").build();
            }
            ObjectId objectId = new ObjectId(id);

            LocalDate date = DateParser.parseDateString(contentBean.getReleaseDate());

            Content content = new Content(
                    contentBean.getTitle(),
                    contentBean.getGenre(),
                    contentBean.getDescription(),
                    contentBean.getStatus(),
                    date,
                    contentBean.getPhotoURL(),
                    contentBean.getAgeRating(),
                    Integer.parseInt(contentBean.getScoreRating()),
                    contentBean.getType());

            boolean isUpdated = contentDao.updateContent(objectId, content);
            if (isUpdated) {
                return Response.status(Response.Status.OK).entity("Content updated successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Content not found with provided ID").build();
            }
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Invalid score rating", e);
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid score rating").build();
        }
    }

    /**
     * Retrieves Content items by their title.
     *
     * @param title The title of the Content items to retrieve.
     * @return a Response containing the list of Content items or an error message.
     */
    @GET
    @Path("/getByTitle/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByTitle(@PathParam("title") String title) {
        logger.log(Level.SEVERE, title);
        try {
            if (title == null || title.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Title is required").build();
            }
            Optional<List<Content>> contentOptional = contentDao.getContentByTitle(title);

            if (contentOptional.isPresent()) {
                return Response.ok(contentOptional.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Content not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving content").build();
        }
    }

    /**
     * Retrieves a specific Content item by its ID.
     *
     * @param id The ID of the Content item to retrieve.
     * @return a Response containing the Content item or an error message.
     */
    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getContent(@PathParam("id") String id) {

        try {
            // Validate and convert the ID
            if (!ObjectId.isValid(id)) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID format").build();
            }
            ObjectId objectId = new ObjectId(id);
            Optional<Content> contentOptional = contentDao.getContent(objectId);

            if (contentOptional.isPresent()) {
                return Response.ok(contentOptional.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Content not found").build();
            }
        } catch (Exception e) {
            // Log the exception
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving content").build();
        }
    }

    /**
     * Deletes a specific Content item from the database.
     *
     * @param id The ID of the Content item to delete.
     * @return a Response indicating the result of the operation.
     */
    @POST
    @Path("/delete/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response deleteContent(@PathParam("id") String id) {
        // Validate and convert the ID
        if (!ObjectId.isValid(id)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID format").build();
        }
        ObjectId objectId = new ObjectId(id);

        // Update the content in the database
        boolean isDeleted = contentDao.deleteContent(objectId);
        if (isDeleted) {
            return Response.status(Response.Status.OK).entity("Content deleted successfully").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Content not found with provided ID").build();
        }
    }

    /**
     * Inner class representing a data transfer object for Content data.
     */
    @Data
    public static class ContentBean {
        @FormParam("title")
        private String title;

        @FormParam("genre")
        private String genre;

        @FormParam("description")
        private String description;

        @FormParam("status")
        private String status;

        @FormParam("releaseDate")
        private String releaseDate;

        @FormParam("photoURL")
        private String photoURL;

        @FormParam("ageRating")
        private String ageRating;

        @FormParam("scoreRating")
        private String scoreRating;

        @FormParam("type")
        private String type;
    }
}

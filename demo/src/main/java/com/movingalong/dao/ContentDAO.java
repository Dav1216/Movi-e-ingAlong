package com.movingalong.dao;

import dev.morphia.Datastore;

import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import com.movingalong.entities.Content;
import com.movingalong.entities.User;
import com.movingalong.utils.UtilsDB;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) for managing Content entities in MongoDB.
 */
public class ContentDAO {
    private final UtilsDB utils;
    private Datastore datastore;
    private static final Logger logger = Logger.getLogger(ContentDAO.class.getName());

    /**
     * Constructor for ContentDAO.
     * Initializes the database and collection for content management.
     */
    public ContentDAO() {
        this.utils = new UtilsDB();
        this.datastore = utils.getDatastore();
    }

    /**
     * Retrieves a list of all Content items from the database.
     *
     * @return a list of Content objects, or an empty list if an error occurs.
     */
    public List<Content> getListContent() {
        try {
            List<Content> contentlist = datastore.find(Content.class).iterator().toList();

            return contentlist;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getListContent", e);
            return new ArrayList<>();
        }
    }

    /**
     * Adds a new Content item to the database.
     *
     * @param content The Content object to be added.
     */
    public void addContent(Content content) {
        if (validateContent(content)) {
            try {
                datastore.save(content);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error in addContent", e);
            }
        }
    }

    /**
     * Updates an existing Content item in the database.
     *
     * @param id      The ObjectId of the Content item to update.
     * @param content The new Content data to be updated.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateContent(ObjectId id, Content content) {
        if (validateContent(content)) {
            try {
                Document query = new Document("_id", id);
                Content oldContent = datastore.find(Content.class, query).first();
                datastore.delete(oldContent);

                datastore.save(content);

                return true;
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error in updateContent", e);
                return false;
            }
        }
        return false;
    }

    /**
     * Retrieves a Content item from the database based on its ObjectId.
     *
     * @param id The ObjectId of the Content item.
     * @return An Optional containing the Content object if found, or an empty
     *         Optional otherwise.
     */
    public Optional<Content> getContent(ObjectId id) {
        try {
            Document query = new Document("_id", id);
            Content content = datastore.find(Content.class, query).first();

            return Optional.ofNullable(content);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getContent", e);
            return Optional.empty();
        }
    }

    /**
     * Retrieves a list of Content items from the database with a specific title.
     *
     * @param title The title of the Content items to retrieve.
     * @return An Optional containing a list of Content objects with the specified
     *         title, or an empty Optional if none found.
     */
    public Optional<List<Content>> getContentByTitle(String title) {
        try {

            Document query = new Document("title", title);
            List<Content> listContents = datastore.find(Content.class, query).iterator().toList();

            return Optional.ofNullable(listContents);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getContent", e);
            return Optional.empty();
        }
    }

    /**
     * Deletes a Content item from the database based on
     * 
     * @param id The ObjectId of the Content item to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteContent(ObjectId id) {
        try {
            Document query = new Document("_id", id);
            User oldContent = datastore.find(User.class, query).first();
            DeleteResult result = datastore.delete(oldContent);

            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in deleteContent", e);
            return false;
        }
    }

    /**
     * Validates a Content object to ensure it meets certain criteria before being
     * persisted to the database.
     *
     * @param content The Content object to validate.
     * @return true if the Content object is valid, false otherwise.
     */
    private boolean validateContent(Content content) {
        if (content == null || content.getTitle() == null || content.getTitle().isEmpty()) {
            logger.log(Level.WARNING, "Content validation failed");
            return false;
        }
        return true;
    }
}
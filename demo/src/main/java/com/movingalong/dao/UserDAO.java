package com.movingalong.dao;

import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import com.movingalong.entities.User;
import com.movingalong.utils.UtilsDB;

import dev.morphia.Datastore;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) for managing User entities in MongoDB.
 */
public class UserDAO {

    private final UtilsDB utils;
    private Datastore datastore;
    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());

    /**
     * Constructor for UserDAO.
     * Initializes the database and collection for user management.
     */
    public UserDAO() {
        this.utils = new UtilsDB();
        this.datastore = utils.getDatastore();
    }

    public List<User> getListUsers() {
        try {
            List<User> userList = datastore.find(User.class).iterator().toList();

            return userList;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getListUsers", e);
            return new ArrayList<>();
        }
    }

    /**
     * Adds a new user to the database.
     *
     * @param user The User object to be added.
     */
    public void addUser(User user) {
        if (validateUser(user)) {
            try {
                datastore.save(user); 
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error in addUser", e);
            }
        }
    }

    /**
     * Updates an existing user in the database.
     *
     * @param id   The ObjectId of the user to update.
     * @param user The new user data to be updated.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateUser(ObjectId id, User user) {
        if (validateUser(user)) {
            try {
                Document query = new Document("_id", id);
                User oldUser = datastore.find(User.class, query).first();
                datastore.delete(oldUser);

                datastore.save(user);

                return true;
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error in updateUser", e);
                return false; 
            }
        }
        return false;
    }

    /**
     * Retrieves a User from the database based on its ObjectId.
     *
     * @param id The ObjectId of the user.
     * @return An Optional containing the User object if found, or an empty
     *         Optional otherwise.
     */
    public Optional<User> getUser(ObjectId id) {
        try {
            Document query = new Document("_id", id);
            User user = datastore.find(User.class, query).first();

            return Optional.ofNullable(user);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getUser", e);
            return Optional.empty();
        }
    }

    /**
     * Retrieves a User from the database based on their email address.
     *
     * @param email The email of the user.
     * @return An Optional containing the User object if found, or an empty
     *         Optional otherwise.
     */
    public Optional<User> getUserByEmail(String email) {
        try {
            Document query = new Document("email", email);
            User user = datastore.find(User.class, query).first();

            return Optional.ofNullable(user);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getUserByEmail", e);
            return Optional.empty();
        }
    }

    /**
     * Retrieves a User from the database based on its username.
     *
     * @param username The username of the user.
     * @return An Optional containing the User object if found, or an empty
     *         Optional otherwise.
     */
    public Optional<User> getUserByUsername(String username) {
        try {
            Document query = new Document("username", username);
            User user = datastore.find(User.class, query).first();

            return Optional.ofNullable(user);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getUserByUsername", e);
            return Optional.empty();
        }
    }

    /**
     * Deletes a User from the database.
     *
     * @param id The ObjectId of the User to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteUser(ObjectId id) {
        try {
            Document query = new Document("_id", id);
            User oldUser = datastore.find(User.class, query).first();
            DeleteResult result = datastore.delete(oldUser);

            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in deleteUser", e);
            return false;
        }
    }

    /**
     * Authenticates a user with provided credentials (username and password)
     *
     * @param username The username of the user.
     * @param password The plain text password entered by the user.
     * @return An Optional containing the User object if credentials are valid,
     *         or an empty Optional otherwise.
     */
    public Optional<User> authenticateUser(String username, String password) {
        try {
            Optional<User> userOptional = getUserByUsername(username);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                String hashedPassword = user.getPassword();

                // check if passwords match
                if (BCrypt.checkpw(password, hashedPassword)) {
                    return Optional.of(user);
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in authenticateUser", e);
            return Optional.empty();
        }
    }

    /**
     * Validates a User object to ensure it meets certain criteria.
     * 
     * @param user The User object to validate.
     * @return true if the User object is valid, false otherwise.
     */
    private boolean validateUser(User user) {
        if (user == null || user.getUsername() == null || user.getUsername().isEmpty()
                || user.getEmail() == null || user.getEmail().isEmpty()) {
            logger.log(Level.WARNING, "User validation failed");
            return false;
        }
        return true;
    }
}
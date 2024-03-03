package com.movingalong.entities;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

/**
 * Represents a User entity with attributes such as username, date of birth,
 * email, etc.
 */
@Data
@Entity("users")
public class User {
    @Id
    private ObjectId id;
    private String username;
    private String dateBirth;
    private String createdAt;
    private String email;
    private String password;
    private String photoURL;
    private List<String> reviews;

    /**
     * Default constructor for the User class.
     * Initializes the reviews list.
     */
    public User() {
        this.reviews = new ArrayList<>();
    }

    /**
     * Constructor for the User class.
     *
     * @param username  The username of the user.
     * @param dateBirth The date of birth of the user.
     * @param createdAt The creation date of the user's account.
     * @param email     The email address of the user.
     * @param password  The password of the user.
     * @param photoURL  The URL of the user's profile photo.
     */
    public User(String username, String dateBirth, String createdAt, String email, String password, String photoURL) {
        this.username = username;
        this.dateBirth = dateBirth;
        this.createdAt = createdAt;
        this.email = email;
        this.password = password;
        this.photoURL = photoURL;
        this.reviews = new ArrayList<>();
    }
}

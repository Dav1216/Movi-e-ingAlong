package com.movingalong.entities;

import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;

import lombok.Data;

import org.bson.types.ObjectId;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;


/**
 * Represents a content entity with various attributes like title, genre,
 * description, etc.
 */
@Data
@Entity("contents")
public class Content {
    /**
     * The _id of the content.
     */
    @Id 
    private ObjectId id;
    /**
     * The title of the content.
     */
    private String title;

    /**
     * The genre of the content.
     */
    private String genre;

    /**
     * A brief description of the content.
     */
    private String description;

    /**
     * The current status of the content (e.g., released, upcoming).
     */
    private String status;

    /**
     * The release date of the content.
     */
    private LocalDate releaseDate;

    /**
     * A string representing the content's photo's URL.
     */
    private String photoURL;

    /**
     * The age rating of the content (e.g., PG-13, R).
     */
    private String ageRating;

    /**
     * The score rating of the content.
     */
    private Integer scoreRating;

    /**
     * The type of the content (e.g., movie, series).
     */
    private String type;

    /**
     * A list of ObjectIds representing reviews associated with the content.
     */
    private List<ObjectId> reviews;

    /**
     * A list of ObjectIds representing celebrities associated with the content.
     */
    private List<ObjectId> celebrities;

    /**
     * Default constructor for creating a new Content object.
     */
    public Content() {
        this.reviews = new ArrayList<>();
        this.celebrities = new ArrayList<>();
    }

    /**
     * Constructs a new Content object with specified values for its fields.
     *
     * @param title       The title of the content.
     * @param genre       The genre of the content.
     * @param description A brief description of the content.
     * @param status      The current status of the content.
     * @param releaseDate The release date of the content.
     * @param photo       A Base64 encoded string representing the photo of the
     *                    content.
     * @param ageRating   The age rating of the content.
     * @param scoreRating The score rating of the content.
     * @param type        The type of the content.
     */
    public Content(String title, String genre,
            String description, String status,
            LocalDate releaseDate, String photoURL,
            String ageRating, Integer scoreRating, String type) {

        this.title = title;
        this.genre = genre;
        this.description = description;
        this.status = status;
        this.releaseDate = releaseDate;
        this.photoURL = photoURL;
        this.ageRating = ageRating;
        this.scoreRating = scoreRating;
        this.type = type;
        this.reviews = new ArrayList<>();
        this.celebrities = new ArrayList<>();
    }
}

package com.basaki.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * {@code BookRequest} represents a response during book creation.
 * <p/>
 *
 * @author Indra Basak
 * @since 12/7/17
 */
@Data
public class BookRequest {

    private String title;

    private String author;

    @JsonCreator
    public BookRequest(@JsonProperty("title") String title,
            @JsonProperty("author") String author) {
        this.title = title;
        this.author = author;
    }
}

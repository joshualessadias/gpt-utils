package com.joshua.dias.gptutils.message.model;

/**
 * DTO for text message content.
 */
public class TextDTO {
    private String body;

    // Default constructor
    public TextDTO() {
    }

    // Constructor with all fields
    public TextDTO(String body) {
        this.body = body;
    }

    // Getters and setters
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
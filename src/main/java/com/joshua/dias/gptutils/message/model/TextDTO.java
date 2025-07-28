package com.joshua.dias.gptutils.message.model;

/**
 * DTO for text message content.
 */
public class TextDTO {
    private String message;

    // Default constructor
    public TextDTO() {
    }

    // Constructor with all fields
    public TextDTO(String message) {
        this.message = message;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
package com.joshua.dias.gptutils.message.model;

/**
 * DTO for reaction message content.
 */
public class ReactionDTO {
    private String emoji;
    private String messageId;

    // Default constructor
    public ReactionDTO() {
    }

    // Constructor with all fields
    public ReactionDTO(String emoji, String messageId) {
        this.emoji = emoji;
        this.messageId = messageId;
    }

    // Getters and setters
    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
package com.joshua.dias.gptutils.zapi.model;

/**
 * Data Transfer Object for sending messages via Z-API.
 */
public class SendMessageRequestDTO {
    private String phone;
    private String message;
    private Integer delayMessage;
    private Integer delayTyping;
    private String editMessageId;
    private String messageId;

    // Default constructor
    public SendMessageRequestDTO() {
    }

    // Constructor with required fields
    public SendMessageRequestDTO(String phone, String message) {
        this.phone = phone;
        this.message = message;
    }

    // Full constructor
    public SendMessageRequestDTO(String phone, String message, Integer delayMessage, Integer delayTyping, String editMessageId) {
        this.phone = phone;
        this.message = message;
        this.delayMessage = delayMessage;
        this.delayTyping = delayTyping;
        this.editMessageId = editMessageId;
    }

    // Getters and setters
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getDelayMessage() {
        return delayMessage;
    }

    public void setDelayMessage(Integer delayMessage) {
        this.delayMessage = delayMessage;
    }

    public Integer getDelayTyping() {
        return delayTyping;
    }

    public void setDelayTyping(Integer delayTyping) {
        this.delayTyping = delayTyping;
    }

    public String getEditMessageId() {
        return editMessageId;
    }

    public void setEditMessageId(String editMessageId) {
        this.editMessageId = editMessageId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
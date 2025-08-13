package com.joshua.dias.gptutils.zapi.model;

/**
 * Data Transfer Object for marking messages as read via Z-API.
 * Based on the Z-API documentation at https://developer.z-api.io/message/read-message
 */
public class ReadMessageRequestDTO {
    private String phone;
    private String messageId;

    // Default constructor
    public ReadMessageRequestDTO() {
    }

    // Constructor with required fields
    public ReadMessageRequestDTO(String phone, String messageId) {
        this.phone = phone;
        this.messageId = messageId;
    }

    // Getters and setters
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
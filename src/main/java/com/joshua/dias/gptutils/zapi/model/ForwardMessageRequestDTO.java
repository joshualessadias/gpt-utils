package com.joshua.dias.gptutils.zapi.model;

/**
 * Data Transfer Object for forwarding messages via Z-API.
 * Based on the Z-API documentation at https://developer.z-api.io/message/forward-message
 */
public class ForwardMessageRequestDTO {
    private String phone;
    private String messageId;
    private String messagePhone;

    // Default constructor
    public ForwardMessageRequestDTO() {
    }

    // Constructor with required fields
    public ForwardMessageRequestDTO(String phone, String messageId, String messagePhone) {
        this.phone = phone;
        this.messageId = messageId;
        this.messagePhone = messagePhone;
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

    public String getMessagePhone() {
        return messagePhone;
    }

    public void setMessagePhone(String messagePhone) {
        this.messagePhone = messagePhone;
    }
}
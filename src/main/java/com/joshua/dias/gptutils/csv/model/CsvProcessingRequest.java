package com.joshua.dias.gptutils.csv.model;

/**
 * Request model for CSV processing.
 */
public class CsvProcessingRequest {
    private String phoneNumber;
    private String documentUrl;
    private String messageId;

    // Default constructor
    public CsvProcessingRequest() {
    }

    // Constructor with all fields
    public CsvProcessingRequest(String phoneNumber, String documentUrl, String messageId) {
        this.phoneNumber = phoneNumber;
        this.documentUrl = documentUrl;
        this.messageId = messageId;
    }

    // Getters and setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
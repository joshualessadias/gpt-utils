package com.joshua.dias.gptutils.transcription.model;

/**
 * Represents a response from the audio transcription service.
 * Contains the transcribed text, metadata about the transcription, and the original message ID for referencing.
 */
public class TranscriptionResponse {
    private String phoneNumber;
    private String transcribedText;
    private boolean success;
    private String errorMessage;
    private String messageId;

    // Default constructor
    public TranscriptionResponse() {
    }

    // Constructor for successful transcription
    public TranscriptionResponse(String phoneNumber, String transcribedText) {
        this.phoneNumber = phoneNumber;
        this.transcribedText = transcribedText;
        this.success = true;
        this.errorMessage = null;
    }
    
    // Constructor for successful transcription with messageId
    public TranscriptionResponse(String phoneNumber, String transcribedText, String messageId) {
        this.phoneNumber = phoneNumber;
        this.transcribedText = transcribedText;
        this.success = true;
        this.errorMessage = null;
        this.messageId = messageId;
    }

    // Constructor for failed transcription
    public TranscriptionResponse(String phoneNumber, String errorMessage, boolean success) {
        this.phoneNumber = phoneNumber;
        this.transcribedText = null;
        this.success = success;
        this.errorMessage = errorMessage;
    }
    
    // Constructor for failed transcription with messageId
    public TranscriptionResponse(String phoneNumber, String errorMessage, boolean success, String messageId) {
        this.phoneNumber = phoneNumber;
        this.transcribedText = null;
        this.success = success;
        this.errorMessage = errorMessage;
        this.messageId = messageId;
    }

    // Getters and setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTranscribedText() {
        return transcribedText;
    }

    public void setTranscribedText(String transcribedText) {
        this.transcribedText = transcribedText;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public String getMessageId() {
        return messageId;
    }
    
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
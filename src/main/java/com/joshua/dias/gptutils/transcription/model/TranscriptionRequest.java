package com.joshua.dias.gptutils.transcription.model;

/**
 * Represents a request for audio transcription.
 * Contains the user's phone number, the audio URL to be transcribed, and the original message ID for referencing.
 */
public class TranscriptionRequest {
    private String phoneNumber;
    private String audioUrl;
    private String messageId;

    // Default constructor
    public TranscriptionRequest() {
    }

    // Constructor with all fields
    public TranscriptionRequest(String phoneNumber, String audioUrl) {
        this.phoneNumber = phoneNumber;
        this.audioUrl = audioUrl;
    }
    
    // Constructor with all fields including messageId
    public TranscriptionRequest(String phoneNumber, String audioUrl, String messageId) {
        this.phoneNumber = phoneNumber;
        this.audioUrl = audioUrl;
        this.messageId = messageId;
    }

    // Getters and setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
    
    public String getMessageId() {
        return messageId;
    }
    
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
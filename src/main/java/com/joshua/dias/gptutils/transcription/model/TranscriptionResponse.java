package com.joshua.dias.gptutils.transcription.model;

/**
 * Represents a response from the audio transcription service.
 * Contains the transcribed text and metadata about the transcription.
 */
public class TranscriptionResponse {
    private String phoneNumber;
    private String transcribedText;
    private boolean success;
    private String errorMessage;

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

    // Constructor for failed transcription
    public TranscriptionResponse(String phoneNumber, String errorMessage, boolean success) {
        this.phoneNumber = phoneNumber;
        this.transcribedText = null;
        this.success = success;
        this.errorMessage = errorMessage;
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
}
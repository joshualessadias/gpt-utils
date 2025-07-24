package com.joshua.dias.gptutils.transcription.model;

/**
 * Represents a request for audio transcription.
 * Contains the user's phone number and the audio URL to be transcribed.
 */
public class TranscriptionRequest {
    private String phoneNumber;
    private String audioUrl;

    // Default constructor
    public TranscriptionRequest() {
    }

    // Constructor with all fields
    public TranscriptionRequest(String phoneNumber, String audioUrl) {
        this.phoneNumber = phoneNumber;
        this.audioUrl = audioUrl;
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
}
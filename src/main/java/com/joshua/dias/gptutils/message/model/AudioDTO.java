package com.joshua.dias.gptutils.message.model;

/**
 * DTO for audio message content.
 */
public class AudioDTO {
    private String audioUrl;
    private String mimeType;
    private long duration;

    // Default constructor
    public AudioDTO() {
    }

    // Constructor with all fields
    public AudioDTO(String audioUrl, String mimeType, long duration) {
        this.audioUrl = audioUrl;
        this.mimeType = mimeType;
        this.duration = duration;
    }

    // Getters and setters
    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
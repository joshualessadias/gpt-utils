package com.joshua.dias.gptutils.message.model;

/**
 * DTO for video message content.
 */
public class VideoDTO {
    private String videoUrl;
    private String caption;
    private String mimeType;
    private int width;
    private int height;
    private int seconds;
    private boolean viewOnce;
    private boolean isGif;

    // Default constructor
    public VideoDTO() {
    }

    // Constructor with all fields
    public VideoDTO(String videoUrl, String caption, String mimeType, int width, int height, int seconds, boolean viewOnce, boolean isGif) {
        this.videoUrl = videoUrl;
        this.caption = caption;
        this.mimeType = mimeType;
        this.width = width;
        this.height = height;
        this.seconds = seconds;
        this.viewOnce = viewOnce;
        this.isGif = isGif;
    }

    // Getters and setters
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public boolean isViewOnce() {
        return viewOnce;
    }

    public void setViewOnce(boolean viewOnce) {
        this.viewOnce = viewOnce;
    }

    public boolean isGif() {
        return isGif;
    }

    public void setGif(boolean gif) {
        isGif = gif;
    }
}
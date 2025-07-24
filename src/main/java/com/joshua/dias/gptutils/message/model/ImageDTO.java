package com.joshua.dias.gptutils.message.model;

/**
 * DTO for image message content.
 */
public class ImageDTO {
    private String imageUrl;
    private String caption;
    private String mimeType;

    // Default constructor
    public ImageDTO() {
    }

    // Constructor with all fields
    public ImageDTO(String imageUrl, String caption, String mimeType) {
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.mimeType = mimeType;
    }

    // Getters and setters
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
}
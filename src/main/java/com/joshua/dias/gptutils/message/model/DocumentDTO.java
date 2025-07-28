package com.joshua.dias.gptutils.message.model;

/**
 * DTO for document message content.
 */
public class DocumentDTO {
    private String documentUrl;
    private String mimeType;
    private String fileName;
    private long fileSize;

    // Default constructor
    public DocumentDTO() {
    }

    // Constructor with all fields
    public DocumentDTO(String documentUrl, String mimeType, String fileName, long fileSize) {
        this.documentUrl = documentUrl;
        this.mimeType = mimeType;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    // Getters and setters
    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
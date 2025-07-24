package com.joshua.dias.gptutils.transcription.service;

import java.io.File;
import java.io.IOException;

/**
 * Service interface for audio compression.
 * Following the Single Responsibility Principle, this interface focuses solely on audio compression.
 */
public interface AudioCompressionService {
    
    /**
     * Maximum content size allowed by OpenAI in bytes (25MB)
     */
    long MAX_CONTENT_SIZE = 26214400;
    
    /**
     * Compresses an audio file if it exceeds the maximum allowed size.
     *
     * @param inputFile The audio file to compress
     * @return The compressed audio file, or the original file if compression was not needed
     * @throws IOException If an error occurs during compression
     */
    File compressIfNeeded(File inputFile) throws IOException;
    
    /**
     * Checks if the file needs compression based on its size.
     *
     * @param file The file to check
     * @return true if the file exceeds the maximum allowed size, false otherwise
     */
    default boolean needsCompression(File file) {
        return file != null && file.exists() && file.length() > MAX_CONTENT_SIZE;
    }
}
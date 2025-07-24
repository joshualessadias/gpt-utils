package com.joshua.dias.gptutils.transcription.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Implementation of AudioCompressionService that uses FFmpeg (via Jave2) to compress audio files.
 * This service compresses audio files that exceed the maximum size allowed by OpenAI.
 */
@ApplicationScoped
public class FFmpegAudioCompressionService implements AudioCompressionService {

    private static final Logger LOG = Logger.getLogger(FFmpegAudioCompressionService.class);
    
    // Compression quality levels
    private static final int HIGH_QUALITY_BITRATE = 128000; // 128kbps
    private static final int MEDIUM_QUALITY_BITRATE = 96000; // 96kbps
    private static final int LOW_QUALITY_BITRATE = 64000; // 64kbps
    private static final int VERY_LOW_QUALITY_BITRATE = 32000; // 32kbps
    
    @Override
    public File compressIfNeeded(File inputFile) throws IOException {
        if (!needsCompression(inputFile)) {
            LOG.info("File size is within limits, no compression needed: " + inputFile.length() + " bytes");
            return inputFile;
        }
        
        LOG.info("File exceeds size limit (" + inputFile.length() + " bytes), compressing...");
        
        // Try compression with different quality levels until file size is acceptable
        File compressedFile = tryCompression(inputFile, HIGH_QUALITY_BITRATE);
        
        if (needsCompression(compressedFile)) {
            LOG.info("High quality compression not sufficient, trying medium quality...");
            File mediumQualityFile = tryCompression(inputFile, MEDIUM_QUALITY_BITRATE);
            // Delete the previous compressed file
            if (compressedFile != inputFile && compressedFile.exists()) {
                compressedFile.delete();
            }
            compressedFile = mediumQualityFile;
        }
        
        if (needsCompression(compressedFile)) {
            LOG.info("Medium quality compression not sufficient, trying low quality...");
            File lowQualityFile = tryCompression(inputFile, LOW_QUALITY_BITRATE);
            // Delete the previous compressed file
            if (compressedFile != inputFile && compressedFile.exists()) {
                compressedFile.delete();
            }
            compressedFile = lowQualityFile;
        }
        
        if (needsCompression(compressedFile)) {
            LOG.info("Low quality compression not sufficient, trying very low quality...");
            File veryLowQualityFile = tryCompression(inputFile, VERY_LOW_QUALITY_BITRATE);
            // Delete the previous compressed file
            if (compressedFile != inputFile && compressedFile.exists()) {
                compressedFile.delete();
            }
            compressedFile = veryLowQualityFile;
        }
        
        if (needsCompression(compressedFile)) {
            LOG.warn("Could not compress file below the size limit even with very low quality. Using the best compression achieved.");
        }
        
        LOG.info("Compression complete. Original size: " + inputFile.length() + " bytes, Compressed size: " + compressedFile.length() + " bytes");
        return compressedFile;
    }
    
    /**
     * Attempts to compress the audio file with the specified bitrate.
     *
     * @param inputFile The audio file to compress
     * @param bitrate The target bitrate for compression
     * @return The compressed file, or the original file if compression failed
     */
    private File tryCompression(File inputFile, int bitrate) throws IOException {
        try {
            // Create a temporary file for the compressed output
            String extension = getFileExtension(inputFile.getName());
            String outputFileName = "compressed-" + UUID.randomUUID() + "." + extension;
            File outputFile = new File(System.getProperty("java.io.tmpdir"), outputFileName);
            
            // Set up the audio attributes for compression
            AudioAttributes audioAttributes = new AudioAttributes();
            audioAttributes.setBitRate(bitrate);
            audioAttributes.setChannels(1); // Mono audio (reduces file size)
            audioAttributes.setSamplingRate(22050); // Lower sampling rate (reduces file size)
            
            // Set up the encoding attributes
            EncodingAttributes encodingAttributes = new EncodingAttributes();
            encodingAttributes.setAudioAttributes(audioAttributes);
            encodingAttributes.setOutputFormat(extension);
            
            // Perform the compression
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(inputFile), outputFile, encodingAttributes);
            
            return outputFile;
        } catch (EncoderException e) {
            LOG.error("Error compressing audio file: " + e.getMessage(), e);
            throw new IOException("Failed to compress audio file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Extracts the file extension from a filename.
     *
     * @param fileName The filename
     * @return The file extension, or "mp3" if no extension is found
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "mp3"; // Default extension
        }
        
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (extension.isEmpty()) {
            return "mp3"; // Default extension
        }
        
        return extension.toLowerCase();
    }
}
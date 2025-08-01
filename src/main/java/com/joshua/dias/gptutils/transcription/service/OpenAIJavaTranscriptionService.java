package com.joshua.dias.gptutils.transcription.service;

import com.joshua.dias.gptutils.transcription.model.TranscriptionRequest;
import com.joshua.dias.gptutils.transcription.model.TranscriptionResponse;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.audio.transcriptions.Transcription;
import com.openai.models.audio.transcriptions.TranscriptionCreateParams;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

/**
 * Implementation of TranscriptionService that uses the official OpenAI Java library.
 * This implementation follows the example from:
 * https://github.com/openai/openai-java/blob/main/openai-java-example/src/main/java/com/openai/example/AudioTranscriptionsExample.java
 */
@ApplicationScoped
@Alternative
@Priority(1) // Highest priority to ensure this implementation is selected
public class OpenAIJavaTranscriptionService implements TranscriptionService {

    private static final Logger LOG = Logger.getLogger(OpenAIJavaTranscriptionService.class);

    private final OpenAIClient openAIClient;
    private final String model;
    private final AudioCompressionService audioCompressionService;

    /**
     * Constructor that initializes the OpenAI client with the API key from configuration.
     */
    @Inject
    public OpenAIJavaTranscriptionService(
            @ConfigProperty(name = "openai.api-key") String apiKey,
            @ConfigProperty(name = "openai.model") String model,
            AudioCompressionService audioCompressionService
    ) {
        // Create the OpenAI client with the API key
        this.openAIClient = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .build();
        this.model = model;
        this.audioCompressionService = audioCompressionService;
        LOG.info("OpenAIJavaTranscriptionService initialized with model: " + model);
    }

    @Override
    public TranscriptionResponse transcribe(TranscriptionRequest request) {
        File tempFile = null;
        File compressedFile = null;
        try {
            // Get the audio URL from the request
            String audioUrl = request.getAudioUrl();
            LOG.info("Downloading audio from URL: " + audioUrl);

            // Extract file extension from URL
            String extension = getFileExtensionFromUrl(audioUrl);

            // Download the audio file from the URL
            tempFile = downloadAudioFromUrl(audioUrl, extension);
            
            // Compress the audio file if needed
            LOG.info("Checking if audio file needs compression. Size: " + tempFile.length() + " bytes");
            compressedFile = audioCompressionService.compressIfNeeded(tempFile);

            // If the compressed file is different from the original, log the compression ratio
            if (compressedFile != tempFile) {
                double compressionRatio = (double) compressedFile.length() / tempFile.length() * 100;
                LOG.info(String.format("Audio compressed. Original: %d bytes, Compressed: %d bytes, Ratio: %.2f%%",
                        tempFile.length(), compressedFile.length(), compressionRatio));
            }

            LOG.info("Transcribing audio file with whisper-1 model");
            // Create the transcription parameters with the compressed file
            TranscriptionCreateParams createParams = TranscriptionCreateParams.builder()
                    .file(compressedFile.toPath())
                    .model(model) // Using the model from configuration
                    .build();

            // Call the OpenAI API to transcribe the audio
            Transcription transcription = openAIClient.audio().transcriptions().create(createParams).asTranscription();

            // Return the successful response
            return new TranscriptionResponse(request.getPhoneNumber(), transcription.text(), request.getMessageId());

        } catch (MalformedURLException e) {
            LOG.error("Invalid URL: " + e.getMessage(), e);
            return new TranscriptionResponse(request.getPhoneNumber(), "Invalid URL: " + e.getMessage(), false, request.getMessageId());
        } catch (IOException e) {
            LOG.error("Error downloading or processing audio file: " + e.getMessage(), e);
            return new TranscriptionResponse(request.getPhoneNumber(),
                    "Error downloading or processing audio file: " + e.getMessage(),
                    false, request.getMessageId());
        } catch (Exception e) {
            LOG.error("Unexpected error during transcription: " + e.getMessage(), e);
            return new TranscriptionResponse(request.getPhoneNumber(),
                    "Unexpected error during transcription: " + e.getMessage(),
                    false, request.getMessageId());
        } finally {
            // Clean up the temporary files
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
            // Only delete the compressed file if it's different from the original
            if (compressedFile != null && compressedFile != tempFile && compressedFile.exists()) {
                compressedFile.delete();
            }
        }
    }

    /**
     * Downloads audio from a URL and saves it to a temporary file.
     */
    private File downloadAudioFromUrl(String audioUrl, String extension) throws IOException {
        // Generate a unique file name
        String fileName = "audio-" + UUID.randomUUID() + "." + extension;
        File tempFile = new File(System.getProperty("java.io.tmpdir"), fileName);

        // Create URL and open connection
        URL url = URI.create(audioUrl).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Download the file
        try (InputStream inputStream = connection.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        return tempFile;
    }

    /**
     * Extracts the file extension from a URL.
     */
    private String getFileExtensionFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            return "mp3"; // Default extension
        }

        // Remove query parameters if present
        String cleanUrl = url;
        if (url.contains("?")) {
            cleanUrl = url.substring(0, url.indexOf("?"));
        }

        // Extract extension from the path
        if (cleanUrl.contains(".")) {
            String extension = cleanUrl.substring(cleanUrl.lastIndexOf(".") + 1);
            // Check if the extension is valid (no slashes, reasonable length)
            if (!extension.contains("/") && !extension.isEmpty() && extension.length() <= 5) {
                return extension.toLowerCase();
            }
        }

        return "mp3"; // Default extension if none found
    }
}
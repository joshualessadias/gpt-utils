package com.joshua.dias.gptutils.transcription.service;

import com.joshua.dias.gptutils.orchestration.model.ToolExecutionRequest;
import com.joshua.dias.gptutils.orchestration.model.ToolExecutionResponse;
import com.joshua.dias.gptutils.orchestration.service.Tool;
import com.joshua.dias.gptutils.transcription.model.TranscriptionRequest;
import com.joshua.dias.gptutils.transcription.model.TranscriptionResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Tool implementation for audio transcription.
 * This class integrates the transcription domain with the orchestration domain.
 */
@ApplicationScoped
public class TranscriptionTool implements Tool {
    
    private static final Logger LOG = Logger.getLogger(TranscriptionTool.class);
    private static final String TOOL_NAME = "transcription";
    
    private final TranscriptionToolService transcriptionToolService;
    
    /**
     * Constructor that injects dependencies.
     */
    @Inject
    public TranscriptionTool(TranscriptionToolService transcriptionToolService) {
        this.transcriptionToolService = transcriptionToolService;
        LOG.info("TranscriptionTool initialized");
    }
    
    @Override
    public String getName() {
        return TOOL_NAME;
    }
    
    @Override
    public String getDescription() {
        return "Transcribes audio from a URL to text using OpenAI's whisper-1 model";
    }

    @Override
    public ToolExecutionResponse execute(ToolExecutionRequest request) {
        String requestId = UUID.randomUUID().toString();
        LOG.info("Executing transcription tool synchronously, requestId: " + requestId);

        try {
            // Convert from orchestration model to transcription model
            TranscriptionRequest transcriptionRequest = createTranscriptionRequest(request);

            // Process the transcription asynchronously
            transcriptionToolService.process(transcriptionRequest);

            // Since the transcription service is designed to be fully asynchronous and
            // doesn't provide a way to get the result synchronously, we return an
            // "accepted" response instead of waiting for the result
            return ToolExecutionResponse.accepted(TOOL_NAME, requestId);

        } catch (Exception e) {
            LOG.error("Error executing transcription tool: " + e.getMessage(), e);
            return ToolExecutionResponse.failed(TOOL_NAME, "Error executing transcription: " + e.getMessage(), requestId);
        }
    }
    
    @Override
    public CompletableFuture<ToolExecutionResponse> executeAsync(ToolExecutionRequest request) {
        String requestId = UUID.randomUUID().toString();
        LOG.info("Executing transcription tool asynchronously, requestId: " + requestId);
        
        CompletableFuture<ToolExecutionResponse> resultFuture = new CompletableFuture<>();
        
        try {
            // Convert from orchestration model to transcription model
            TranscriptionRequest transcriptionRequest = createTranscriptionRequest(request);
            
            // Process the transcription asynchronously
            transcriptionToolService.processAsync(transcriptionRequest);
            
            // Since the transcription service is designed to be fully asynchronous and
            // doesn't provide a way to get the result, we complete the future with an
            // "accepted" response immediately
            resultFuture.complete(ToolExecutionResponse.accepted(TOOL_NAME, requestId));
            
        } catch (Exception e) {
            LOG.error("Error executing transcription tool asynchronously: " + e.getMessage(), e);
            resultFuture.complete(
                    ToolExecutionResponse.failed(TOOL_NAME, "Error executing transcription: " + e.getMessage(), requestId));
        }
        
        return resultFuture;
    }
    
    @Override
    public String validateParameters(ToolExecutionRequest request) {
        if (request.getParameters() == null) {
            return "Parameters are required";
        }
        
        String phoneNumber = request.getParameterAs("phoneNumber", null);
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return "Phone number is required";
        }
        
        String audioUrl = request.getParameterAs("contentUrl", null);
        if (audioUrl == null || audioUrl.trim().isEmpty()) {
            return "Audio URL is required";
        }
        
        // Basic URL validation
        if (!audioUrl.startsWith("http://") && !audioUrl.startsWith("https://")) {
            return "Audio URL must be a valid HTTP or HTTPS URL";
        }
        
        return null; // Valid
    }
    
    /**
     * Creates a TranscriptionRequest from a ToolExecutionRequest.
     */
    private TranscriptionRequest createTranscriptionRequest(ToolExecutionRequest request) {
        String phoneNumber = request.getParameterAs("phoneNumber", "");
        String audioUrl = request.getParameterAs("contentUrl", "");
        String messageId = request.getParameterAs("messageId", "");
        
        return new TranscriptionRequest(phoneNumber, audioUrl, messageId);
    }
}
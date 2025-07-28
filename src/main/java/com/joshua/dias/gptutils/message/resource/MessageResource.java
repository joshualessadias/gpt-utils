package com.joshua.dias.gptutils.message.resource;

import com.joshua.dias.gptutils.message.model.ReceiveMessageDTO;
import com.joshua.dias.gptutils.message.service.PhoneToolMappingService;
import com.joshua.dias.gptutils.orchestration.model.ToolExecutionRequest;
import com.joshua.dias.gptutils.orchestration.model.ToolExecutionResponse;
import com.joshua.dias.gptutils.orchestration.service.ToolExecutionService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * REST resource for handling incoming messages.
 * This resource exposes an endpoint for receiving messages and processing them.
 */
@Path("/api/messages")
@ApplicationScoped
public class MessageResource {
    
    private static final Logger LOG = Logger.getLogger(MessageResource.class);
    private static final int DEFAULT_TIMEOUT_SECONDS = 30;
    
    private final ToolExecutionService toolExecutionService;
    private final PhoneToolMappingService phoneToolMappingService;
    
    /**
     * Constructor that injects dependencies.
     */
    @Inject
    public MessageResource(ToolExecutionService toolExecutionService, PhoneToolMappingService phoneToolMappingService) {
        this.toolExecutionService = toolExecutionService;
        this.phoneToolMappingService = phoneToolMappingService;
        LOG.info("MessageResource initialized");
    }
    
    /**
     * Receives a message and processes it.
     * 
     * @param message The message to process
     * @return HTTP response indicating the result of the processing
     */
    @POST
    @Path("/receive")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiveMessage(ReceiveMessageDTO message) {
        try {
            LOG.info("Received message with ID: " + message.getMessageId());
            
            // Validate participant phone
            if (!phoneToolMappingService.isPhoneAllowed(message.getPhone())) {
                LOG.warn("Message blocked: phone " + message.getPhone() + " is not allowed.");
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(createErrorResponse("Message blocked: unauthorized participant phone"))
                        .build();
            }
            
            // Check if audio, video, document, or text is present
            String contentUrl = null;
            String contentType = null;
            String messageContent = null;
            
            // First check for audio content
            if (message.getAudio() != null && message.getAudio().getAudioUrl() != null) {
                contentUrl = message.getAudio().getAudioUrl();
                contentType = "audio";
                LOG.info("Processing audio content with URL: " + contentUrl);
            } 
            // If no audio, check for video content
            else if (message.getVideo() != null && message.getVideo().getVideoUrl() != null) {
                contentUrl = message.getVideo().getVideoUrl();
                contentType = "video";
                LOG.info("Processing video content with URL: " + contentUrl);
            }
            // If no audio or video, check for document content
            else if (message.getDocument() != null && message.getDocument().getDocumentUrl() != null) {
                contentUrl = message.getDocument().getDocumentUrl();
                contentType = "document";
                LOG.info("Processing document content with URL: " + contentUrl);
            }
            // If no audio, video, or document, check for text content
            else if (message.getText() != null && message.getText().getMessage() != null) {
                messageContent = message.getText().getMessage();
                contentType = "text";
                LOG.info("Processing text message: " + messageContent);
            }
            // If no content is present, return an error
            else {
                LOG.warn("Message does not contain audio, video, document, or text content");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(createErrorResponse("Message does not contain audio, video, document, or text content"))
                        .build();
            }
            
            // Extract required parameters
            String phoneNumber = message.getPhone();
            
            // Validate extracted parameters
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                LOG.warn("Phone number is missing or empty");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(createErrorResponse("Phone number is required"))
                        .build();
            }
            
            // For non-text messages, validate content URL
            if (!"text".equals(contentType) && (contentUrl == null || contentUrl.trim().isEmpty())) {
                LOG.warn("Content URL is missing or empty for non-text message");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(createErrorResponse("Content URL is required for non-text messages"))
                        .build();
            }
            
            // For text messages, validate message content
            if ("text".equals(contentType) && (messageContent == null || messageContent.trim().isEmpty())) {
                LOG.warn("Message content is missing or empty for text message");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(createErrorResponse("Message content is required for text messages"))
                        .build();
            }
            
            // Get the tool name for this phone
            String toolName = phoneToolMappingService.getToolForPhone(phoneNumber);
            
            // Validate that we have a tool to execute
            if (toolName == null || toolName.trim().isEmpty()) {
                LOG.error("No tool available for phone: " + phoneNumber + ". This should not happen with forwarding fallback.");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(createErrorResponse("Internal error: No tool available for this phone number"))
                        .build();
            }
            
            // Create tool execution request
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("phoneNumber", phoneNumber);
            parameters.put("contentType", contentType);
            parameters.put("messageId", message.getMessageId());
            
            // Add content URL for non-text messages
            if (!"text".equals(contentType)) {
                parameters.put("contentUrl", contentUrl);
            }
            
            // Add message content for text messages
            if ("text".equals(contentType)) {
                parameters.put("messageContent", messageContent);
            }
            
            // Add sender information if available
            if (message.getSenderName() != null) {
                parameters.put("senderName", message.getSenderName());
            }
            
            ToolExecutionRequest toolRequest = new ToolExecutionRequest(
                    toolName, 
                    parameters, 
                    null);  // No callback URL
            
            // Execute the transcription tool asynchronously
            CompletableFuture<ToolExecutionResponse> future = toolExecutionService.executeAsync(toolRequest);
            
            try {
                // Wait for the result with a timeout
                ToolExecutionResponse response = future.get(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);

                // Return appropriate HTTP response based on the execution status
                return createResponseFromToolExecution(response);

            } catch (InterruptedException | ExecutionException e) {
                LOG.error("Error in async execution: " + e.getMessage(), e);
                return Response.serverError()
                        .entity(createErrorResponse("Error in async execution: " + e.getMessage()))
                        .build();
            } catch (TimeoutException e) {
                LOG.info("Async execution timed out after " + DEFAULT_TIMEOUT_SECONDS + " seconds");

                // Continue execution in the background
                future.thenAccept(response ->
                    LOG.info("Background execution completed with status: " + response.getStatus()));

                // Return an accepted response
                Map<String, Object> response = new HashMap<>();
                response.put("status", "accepted");
                response.put("message", "Request is being processed asynchronously");
                response.put("toolName", toolName);

                return Response.accepted(response).build();
            }
            
        } catch (Exception e) {
            LOG.error("Error processing message: " + e.getMessage(), e);
            return Response.serverError()
                    .entity(createErrorResponse("Server error: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * Creates an HTTP response from a tool execution response.
     */
    private Response createResponseFromToolExecution(ToolExecutionResponse response) {
        switch (response.getStatus()) {
            case COMPLETED:
                return Response.ok(response).build();
            case ACCEPTED:
                return Response.accepted(response).build();
            case REJECTED:
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            case FAILED:
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
            default:
                return Response.serverError().entity(response).build();
        }
    }
    
    /**
     * Creates an error response map.
     */
    private Map<String, Object> createErrorResponse(String errorMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", errorMessage);
        return response;
    }
}
package com.joshua.dias.gptutils.zapi.service;

import com.joshua.dias.gptutils.zapi.client.ZApiClient;
import com.joshua.dias.gptutils.zapi.model.ForwardMessageRequestDTO;
import com.joshua.dias.gptutils.zapi.model.SendMessageRequestDTO;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import java.util.Map;

/**
 * Service for sending messages via Z-API.
 * This service encapsulates the Z-API logic and provides a clean interface for other parts of the application.
 */
@ApplicationScoped
public class ZApiService {

    private static final Logger LOG = Logger.getLogger(ZApiService.class);
    
    private final ZApiClient zApiClient;
    
    /**
     * Constructor that injects dependencies.
     */
    @Inject
    public ZApiService(@RestClient ZApiClient zApiClient) {
        this.zApiClient = zApiClient;
        LOG.info("ZApiService initialized with Z-API client");
    }
    
    /**
     * Sends a WhatsApp message via Z-API.
     * 
     * @param phoneNumber The phone number to send the message to
     * @param message The message content
     * @return true if message was sent successfully, false otherwise
     */
    public boolean sendMessage(String phoneNumber, String message) {
        return sendMessage(phoneNumber, message, null, null, null);
    }
    
    /**
     * Sends a WhatsApp message via Z-API with additional options.
     * 
     * @param phoneNumber The phone number to send the message to
     * @param message The message content
     * @param delayMessage Delay before sending the message (in milliseconds)
     * @param delayTyping Delay for typing simulation (in milliseconds)
     * @param editMessageId ID of the message to edit (if editing an existing message)
     * @return true if message was sent successfully, false otherwise
     */
    public boolean sendMessage(String phoneNumber, String message, Integer delayMessage, Integer delayTyping, String editMessageId) {
        return sendMessage(phoneNumber, message, delayMessage, delayTyping, editMessageId, null);
    }

    /**
     * Sends a WhatsApp message via Z-API with message referencing.
     *
     * @param phoneNumber The phone number to send the message to
     * @param message The message content
     * @param messageId ID of the message to reference (for replying to a specific message)
     * @return true if message was sent successfully, false otherwise
     */
    public boolean sendMessage(String phoneNumber, String message, String messageId) {
        return sendMessage(phoneNumber, message, null, null, null, messageId);
    }
    
    /**
     * Sends a WhatsApp message via Z-API with additional options and message referencing.
     * 
     * @param phoneNumber The phone number to send the message to
     * @param message The message content
     * @param delayMessage Delay before sending the message (in milliseconds)
     * @param delayTyping Delay for typing simulation (in milliseconds)
     * @param editMessageId ID of the message to edit (if editing an existing message)
     * @param messageId ID of the message to reference (for replying to a specific message)
     * @return true if message was sent successfully, false otherwise
     */
    public boolean sendMessage(String phoneNumber, String message, Integer delayMessage, Integer delayTyping, String editMessageId, String messageId) {
        try {
            LOG.info("Sending WhatsApp message to phone number: " + phoneNumber);
            
            // Create message request
            SendMessageRequestDTO request = new SendMessageRequestDTO(phoneNumber, message, delayMessage, delayTyping, editMessageId);
            
            // Set messageId for referencing if provided
            if (messageId != null && !messageId.isEmpty()) {
                request.setMessageId(messageId);
                LOG.info("Referencing message with ID: " + messageId);
            }
            
            // Send message via Z-API
            Response clientResponse = zApiClient.sendMessage(request);
            
            // Check if message was sent successfully
            boolean success = clientResponse.getStatus() >= 200 && clientResponse.getStatus() < 300;
            if (success) {
                LOG.info("WhatsApp message sent successfully via Z-API");
            } else {
                LOG.warn("Failed to send WhatsApp message via Z-API. Status: " + clientResponse.getStatus());
            }
            
            return success;
            
        } catch (Exception e) {
            LOG.error("Error sending WhatsApp message via Z-API: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Forwards a WhatsApp message via Z-API.
     * 
     * @param phoneNumber The phone number to forward the message to
     * @param messageId The ID of the message to forward
     * @param messagePhone The phone number of the original message sender
     * @return The ID of the forwarded message if successful, null otherwise
     */
    public String forwardMessage(String phoneNumber, String messageId, String messagePhone) {
        try {
            LOG.info("Forwarding WhatsApp message to phone number: " + phoneNumber);
            
            // Validate parameters
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                LOG.warn("Cannot forward message: phone number is null or empty");
                return null;
            }
            
            if (messageId == null || messageId.trim().isEmpty()) {
                LOG.warn("Cannot forward message: message ID is null or empty");
                return null;
            }

            if (messagePhone == null || messagePhone.trim().isEmpty()) {
                LOG.warn("Cannot forward message: message phone is null or empty");
                return null;
            }
            
            // Create forward message request
            ForwardMessageRequestDTO request = new ForwardMessageRequestDTO(phoneNumber, messageId, messagePhone);
            
            // Forward message via Z-API
            Response clientResponse = zApiClient.forwardMessage(request);
            
            // Check if message was forwarded successfully
            boolean success = clientResponse.getStatus() >= 200 && clientResponse.getStatus() < 300;
            if (success) {
                LOG.info("WhatsApp message forwarded successfully via Z-API");
                
                // Extract the messageId from the response
                try {
                    Map<String, Object> responseBody = clientResponse.readEntity(Map.class);
                    if (responseBody != null && responseBody.containsKey("messageId")) {
                        String forwardedMessageId = (String) responseBody.get("messageId");
                        LOG.info("Forwarded message ID: " + forwardedMessageId);
                        return forwardedMessageId;
                    } else {
                        LOG.warn("Response does not contain messageId");
                    }
                } catch (Exception e) {
                    LOG.error("Error extracting messageId from response: " + e.getMessage(), e);
                }
            } else {
                LOG.warn("Failed to forward WhatsApp message via Z-API. Status: " + clientResponse.getStatus());
            }
            
            return null;
            
        } catch (Exception e) {
            LOG.error("Error forwarding WhatsApp message via Z-API: " + e.getMessage(), e);
            return null;
        }
    }
}
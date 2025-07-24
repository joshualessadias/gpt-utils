package com.joshua.dias.gptutils.zapi.service;

import com.joshua.dias.gptutils.zapi.client.ZApiClient;
import com.joshua.dias.gptutils.zapi.model.SendMessageRequestDTO;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

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
        try {
            LOG.info("Sending WhatsApp message to phone number: " + phoneNumber);
            
            // Create message request
            SendMessageRequestDTO request = new SendMessageRequestDTO(phoneNumber, message, delayMessage, delayTyping, editMessageId);
            
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
}
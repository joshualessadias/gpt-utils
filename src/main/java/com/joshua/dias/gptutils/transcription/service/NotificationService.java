package com.joshua.dias.gptutils.transcription.service;

import com.joshua.dias.gptutils.transcription.model.TranscriptionResponse;
import com.joshua.dias.gptutils.zapi.service.ZApiService;

import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Service for sending notifications about completed transcriptions.
 * This service is called after transcription is complete to send WhatsApp messages via Z-API.
 */
@ApplicationScoped
public class NotificationService {

    private static final Logger LOG = Logger.getLogger(NotificationService.class);

    private final ZApiService zApiService;
    
    /**
     * Constructor that injects dependencies.
     */
    @Inject
    public NotificationService(ZApiService zApiService) {
        this.zApiService = zApiService;
        LOG.info("NotificationService initialized with Z-API service");
    }
    
    /**
     * Sends a notification about a completed transcription via WhatsApp using Z-API.
     * 
     * @param response The transcription response
     * @return true if message was sent successfully, false otherwise
     */
    public boolean sendNotification(TranscriptionResponse response) {
        try {
            LOG.info("Sending WhatsApp notification for phone number: " + response.getPhoneNumber());
            
            // Format the message with transcription details and timestamp
            String message = formatNotificationMessage(response);
            
            // Send WhatsApp message via Z-API service with messageId for referencing
            boolean success = zApiService.sendMessage(response.getPhoneNumber(), message, null, null, null, response.getMessageId());
            
            if (success) {
                LOG.info("WhatsApp notification sent successfully via Z-API");
            } else {
                LOG.warn("Failed to send WhatsApp notification via Z-API");
            }
            
            return success;
            
        } catch (Exception e) {
            LOG.error("Error sending WhatsApp notification: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Formats the notification message with transcription details.
     * 
     * @param response The transcription response
     * @return The formatted message
     */
    private String formatNotificationMessage(TranscriptionResponse response) {
        StringBuilder message = new StringBuilder();
        if (response.isSuccess()) {
            message.append(response.getTranscribedText());
        } else {
            message.append("*Error:* ").append(response.getErrorMessage());
        }

        return message.toString();
    }
}
package com.joshua.dias.gptutils.message.service;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Default implementation of the PhoneToolMappingService.
 * This class maintains a mapping between phone numbers and tool names.
 */
@ApplicationScoped
public class DefaultPhoneToolMappingService implements PhoneToolMappingService {
    
    private static final Logger LOG = Logger.getLogger(DefaultPhoneToolMappingService.class);
    
    // Initial mappings for phone numbers and tools
    private static final String TRANSCRIPTION_PHONE = "120363419403036758-group";
    private static final String TRANSCRIPTION_TOOL = "transcription";
    
    private static final String CSV_PROCESSING_PHONE = "120363419403036759-group"; // TODO: create group for this tool
    private static final String CSV_PROCESSING_TOOL = "csv-processing";
    
    // Map to store phone-to-tool mappings
    private final Map<String, String> phoneToolMap = new HashMap<>();
    
    /**
     * Initializes the service with default mappings.
     */
    @PostConstruct
    void init() {
        LOG.info("Initializing DefaultPhoneToolMappingService");
        
        // Add the mappings
        addPhoneToolMapping(TRANSCRIPTION_PHONE, TRANSCRIPTION_TOOL);
        addPhoneToolMapping(CSV_PROCESSING_PHONE, CSV_PROCESSING_TOOL);
        
        LOG.info("DefaultPhoneToolMappingService initialized with " + phoneToolMap.size() + " mappings");
    }
    
    @Override
    public boolean isPhoneAllowed(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        return phoneToolMap.containsKey(phoneNumber);
    }
    
    @Override
    public String getToolForPhone(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return null;
        }
        return phoneToolMap.get(phoneNumber);
    }
    
    @Override
    public void addPhoneToolMapping(String phoneNumber, String toolName) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            LOG.warn("Cannot add mapping for null or empty phone number");
            return;
        }
        
        if (toolName == null || toolName.trim().isEmpty()) {
            LOG.warn("Cannot add mapping to null or empty tool name");
            return;
        }
        
        LOG.info("Adding mapping: phone=" + phoneNumber + ", tool=" + toolName);
        phoneToolMap.put(phoneNumber, toolName);
    }
    
    @Override
    public boolean removePhoneToolMapping(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            LOG.warn("Cannot remove mapping for null or empty phone number");
            return false;
        }
        
        LOG.info("Removing mapping for phone: " + phoneNumber);
        return phoneToolMap.remove(phoneNumber) != null;
    }
    
    /**
     * Gets all phone numbers that are allowed to use the system.
     * 
     * @return A set of allowed phone numbers
     */
    public Set<String> getAllowedPhones() {
        return Collections.unmodifiableSet(phoneToolMap.keySet());
    }
    
    /**
     * Gets all mappings between phone numbers and tool names.
     * 
     * @return A map of phone numbers to tool names
     */
    public Map<String, String> getAllMappings() {
        return Collections.unmodifiableMap(phoneToolMap);
    }
}
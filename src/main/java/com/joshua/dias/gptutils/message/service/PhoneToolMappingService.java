package com.joshua.dias.gptutils.message.service;

/**
 * Service interface for mapping phone numbers to tool names.
 * This service is responsible for determining which tool should be executed
 * for a given phone number.
 */
public interface PhoneToolMappingService {
    
    /**
     * Checks if a phone number is allowed to use the system.
     * 
     * @param phoneNumber The phone number to check
     * @return true if the phone number is allowed, false otherwise
     */
    boolean isPhoneAllowed(String phoneNumber);
    
    /**
     * Gets the tool name associated with a phone number.
     * 
     * @param phoneNumber The phone number
     * @return The name of the tool to execute, or null if no tool is associated with the phone number
     */
    String getToolForPhone(String phoneNumber);
    
    /**
     * Adds a mapping between a phone number and a tool.
     * 
     * @param phoneNumber The phone number
     * @param toolName The name of the tool
     */
    void addPhoneToolMapping(String phoneNumber, String toolName);
    
    /**
     * Removes a mapping for a phone number.
     * 
     * @param phoneNumber The phone number
     * @return true if a mapping was removed, false if no mapping existed
     */
    boolean removePhoneToolMapping(String phoneNumber);
}
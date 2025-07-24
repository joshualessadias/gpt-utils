package com.joshua.dias.gptutils.orchestration.model;

import java.util.Map;

/**
 * Represents a request to execute a tool.
 * This is a generic model that can be used for any tool execution request.
 */
public class ToolExecutionRequest {
    
    private String toolName;
    private Map<String, Object> parameters;
    private String callbackUrl;
    
    // Default constructor
    public ToolExecutionRequest() {
    }
    
    // Constructor with all fields
    public ToolExecutionRequest(String toolName, Map<String, Object> parameters, String callbackUrl) {
        this.toolName = toolName;
        this.parameters = parameters;
        this.callbackUrl = callbackUrl;
    }
    
    // Getters and setters
    public String getToolName() {
        return toolName;
    }
    
    public void setToolName(String toolName) {
        this.toolName = toolName;
    }
    
    public Map<String, Object> getParameters() {
        return parameters;
    }
    
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
    
    public String getCallbackUrl() {
        return callbackUrl;
    }
    
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
    
    /**
     * Gets a parameter value as a specific type.
     * 
     * @param <T> The type to cast the parameter to
     * @param name The parameter name
     * @param defaultValue The default value to return if the parameter is not found
     * @return The parameter value, or the default value if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T getParameterAs(String name, T defaultValue) {
        if (parameters == null || !parameters.containsKey(name)) {
            return defaultValue;
        }
        
        Object value = parameters.get(name);
        try {
            return (T) value;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }
}
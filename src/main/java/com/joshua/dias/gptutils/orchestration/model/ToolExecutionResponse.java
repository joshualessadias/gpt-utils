package com.joshua.dias.gptutils.orchestration.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a response from a tool execution.
 * This is a generic model that can be used for any tool execution response.
 */
public class ToolExecutionResponse {
    
    /**
     * Enum representing the status of a tool execution.
     */
    public enum Status {
        ACCEPTED,    // Request was accepted and is being processed asynchronously
        COMPLETED,   // Request was processed successfully
        FAILED,      // Request processing failed
        REJECTED     // Request was rejected (e.g., invalid parameters)
    }
    
    private String toolName;
    private Status status;
    private Map<String, Object> result;
    private String errorMessage;
    private String requestId;
    
    // Default constructor
    public ToolExecutionResponse() {
        this.result = new HashMap<>();
    }
    
    // Constructor for accepted requests
    public ToolExecutionResponse(String toolName, String requestId) {
        this.toolName = toolName;
        this.status = Status.ACCEPTED;
        this.requestId = requestId;
        this.result = new HashMap<>();
    }
    
    // Constructor for completed requests
    public ToolExecutionResponse(String toolName, Map<String, Object> result, String requestId) {
        this.toolName = toolName;
        this.status = Status.COMPLETED;
        this.result = result != null ? result : new HashMap<>();
        this.requestId = requestId;
    }
    
    // Constructor for failed requests
    public ToolExecutionResponse(String toolName, String errorMessage, String requestId) {
        this.toolName = toolName;
        this.status = Status.FAILED;
        this.errorMessage = errorMessage;
        this.requestId = requestId;
        this.result = new HashMap<>();
    }
    
    // Constructor for rejected requests
    public ToolExecutionResponse(String toolName, String errorMessage, Status status, String requestId) {
        this.toolName = toolName;
        this.status = status;
        this.errorMessage = errorMessage;
        this.requestId = requestId;
        this.result = new HashMap<>();
    }
    
    // Getters and setters
    public String getToolName() {
        return toolName;
    }
    
    public void setToolName(String toolName) {
        this.toolName = toolName;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public Map<String, Object> getResult() {
        return result;
    }
    
    public void setResult(Map<String, Object> result) {
        this.result = result != null ? result : new HashMap<>();
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public String getRequestId() {
        return requestId;
    }
    
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    
    /**
     * Adds a result value to the response.
     * 
     * @param key The key for the result value
     * @param value The result value
     * @return This response object for method chaining
     */
    public ToolExecutionResponse addResult(String key, Object value) {
        if (this.result == null) {
            this.result = new HashMap<>();
        }
        this.result.put(key, value);
        return this;
    }
    
    /**
     * Creates a response for an accepted request.
     * 
     * @param toolName The name of the tool
     * @param requestId The ID of the request
     * @return A new response object
     */
    public static ToolExecutionResponse accepted(String toolName, String requestId) {
        return new ToolExecutionResponse(toolName, requestId);
    }
    
    /**
     * Creates a response for a completed request.
     * 
     * @param toolName The name of the tool
     * @param result The result of the tool execution
     * @param requestId The ID of the request
     * @return A new response object
     */
    public static ToolExecutionResponse completed(String toolName, Map<String, Object> result, String requestId) {
        return new ToolExecutionResponse(toolName, result, requestId);
    }
    
    /**
     * Creates a response for a failed request.
     * 
     * @param toolName The name of the tool
     * @param errorMessage The error message
     * @param requestId The ID of the request
     * @return A new response object
     */
    public static ToolExecutionResponse failed(String toolName, String errorMessage, String requestId) {
        return new ToolExecutionResponse(toolName, errorMessage, requestId);
    }
    
    /**
     * Creates a response for a rejected request.
     * 
     * @param toolName The name of the tool
     * @param errorMessage The error message
     * @param requestId The ID of the request
     * @return A new response object
     */
    public static ToolExecutionResponse rejected(String toolName, String errorMessage, String requestId) {
        return new ToolExecutionResponse(toolName, errorMessage, Status.REJECTED, requestId);
    }
}
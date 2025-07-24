package com.joshua.dias.gptutils.orchestration.service;

import com.joshua.dias.gptutils.orchestration.model.ToolExecutionRequest;
import com.joshua.dias.gptutils.orchestration.model.ToolExecutionResponse;

import java.util.concurrent.CompletableFuture;

/**
 * Interface for tool implementations.
 * Each tool that wants to be part of the orchestration system must implement this interface.
 */
public interface Tool {
    
    /**
     * Gets the name of the tool.
     * This name is used to identify the tool in execution requests.
     * 
     * @return The name of the tool
     */
    String getName();
    
    /**
     * Gets a description of the tool.
     * 
     * @return A description of what the tool does
     */
    String getDescription();
    
    /**
     * Executes the tool synchronously.
     * 
     * @param request The tool execution request
     * @return The tool execution response
     */
    ToolExecutionResponse execute(ToolExecutionRequest request);
    
    /**
     * Executes the tool asynchronously.
     * 
     * @param request The tool execution request
     * @return A CompletableFuture that will be completed with the tool execution response
     */
    CompletableFuture<ToolExecutionResponse> executeAsync(ToolExecutionRequest request);
    
    /**
     * Validates the parameters for a tool execution request.
     * 
     * @param request The tool execution request
     * @return null if the parameters are valid, an error message otherwise
     */
    String validateParameters(ToolExecutionRequest request);
}
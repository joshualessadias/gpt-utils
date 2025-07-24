package com.joshua.dias.gptutils.orchestration.service;

import com.joshua.dias.gptutils.orchestration.model.ToolExecutionRequest;
import com.joshua.dias.gptutils.orchestration.model.ToolExecutionResponse;

import java.util.concurrent.CompletableFuture;

/**
 * Service interface for executing tools.
 * This is the core of the orchestration system, responsible for routing
 * tool execution requests to the appropriate handlers.
 */
public interface ToolExecutionService {
    
    /**
     * Executes a tool asynchronously.
     * 
     * @param request The tool execution request
     * @return A CompletableFuture that will be completed with the tool execution response
     */
    CompletableFuture<ToolExecutionResponse> executeAsync(ToolExecutionRequest request);
    
    /**
     * Checks if a tool is supported.
     * 
     * @param toolName The name of the tool
     * @return true if the tool is supported, false otherwise
     */
    boolean isToolSupported(String toolName);
}
package com.joshua.dias.gptutils.orchestration.service;

import com.joshua.dias.gptutils.orchestration.model.ToolExecutionRequest;
import com.joshua.dias.gptutils.orchestration.model.ToolExecutionResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Default implementation of the ToolExecutionService.
 * This class uses the ToolRegistry to look up tools by name and delegates execution
 * to the appropriate tool implementation.
 */
@ApplicationScoped
public class DefaultToolExecutionService implements ToolExecutionService {
    
    private static final Logger LOG = Logger.getLogger(DefaultToolExecutionService.class);
    
    private final ToolRegistry toolRegistry;

    /**
     * Constructor that injects dependencies.
     */
    @Inject
    public DefaultToolExecutionService(ToolRegistry toolRegistry) {
        this.toolRegistry = toolRegistry;
        LOG.info("DefaultToolExecutionService initialized");
    }
    
    @Override
    public CompletableFuture<ToolExecutionResponse> executeAsync(ToolExecutionRequest request) {
        String toolName = request.getToolName();
        String requestId = generateRequestId();
        LOG.info("Executing tool '" + toolName + "' asynchronously, requestId: " + requestId);
        
        // Check if the tool is supported
        if (!isToolSupported(toolName)) {
            LOG.warn("Tool '" + toolName + "' is not supported");
            return CompletableFuture.completedFuture(
                    ToolExecutionResponse.rejected(toolName, "Tool '" + toolName + "' is not supported", requestId));
        }
        
        Tool tool = toolRegistry.getTool(toolName);
        
        // Validate parameters
        String validationError = tool.validateParameters(request);
        if (validationError != null) {
            LOG.warn("Invalid parameters for tool '" + toolName + "': " + validationError);
            return CompletableFuture.completedFuture(
                    ToolExecutionResponse.rejected(toolName, validationError, requestId));
        }
        
        try {
            // Execute the tool asynchronously
            return tool.executeAsync(request);
        } catch (Exception e) {
            LOG.error("Error executing tool '" + toolName + "' asynchronously: " + e.getMessage(), e);
            return CompletableFuture.completedFuture(
                    ToolExecutionResponse.failed(toolName, "Error executing tool: " + e.getMessage(), requestId));
        }
    }
    
    @Override
    public boolean isToolSupported(String toolName) {
        return toolRegistry.hasTool(toolName);
    }
    
    /**
     * Generates a unique request ID.
     * 
     * @return A unique request ID
     */
    private String generateRequestId() {
        return UUID.randomUUID().toString();
    }
}
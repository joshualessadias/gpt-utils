package com.joshua.dias.gptutils.csv.service;

import com.joshua.dias.gptutils.csv.model.CsvProcessingRequest;
import com.joshua.dias.gptutils.orchestration.model.ToolExecutionRequest;
import com.joshua.dias.gptutils.orchestration.model.ToolExecutionResponse;
import com.joshua.dias.gptutils.orchestration.service.Tool;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Tool implementation for CSV processing.
 * This class integrates the CSV processing domain with the orchestration domain.
 */
@ApplicationScoped
public class CsvProcessingTool implements Tool {
    
    private static final Logger LOG = Logger.getLogger(CsvProcessingTool.class);
    private static final String TOOL_NAME = "csv-processing";
    
    private final CsvProcessingService csvProcessingService;
    
    /**
     * Constructor that injects dependencies.
     */
    @Inject
    public CsvProcessingTool(CsvProcessingService csvProcessingService) {
        this.csvProcessingService = csvProcessingService;
        LOG.info("CsvProcessingTool initialized");
    }
    
    @Override
    public String getName() {
        return TOOL_NAME;
    }
    
    @Override
    public String getDescription() {
        return "Processes CSV documents, renames columns, maps to JSON, and filters data based on specific criteria";
    }

    @Override
    public ToolExecutionResponse execute(ToolExecutionRequest request) {
        String requestId = UUID.randomUUID().toString();
        LOG.info("Executing CSV processing tool synchronously, requestId: " + requestId);

        try {
            // Convert from orchestration model to CSV processing model
            CsvProcessingRequest csvRequest = createCsvProcessingRequest(request);

            // Process the CSV asynchronously
            csvProcessingService.process(csvRequest);

            // Return an "accepted" response since processing is asynchronous
            return ToolExecutionResponse.accepted(TOOL_NAME, requestId);

        } catch (Exception e) {
            LOG.error("Error executing CSV processing tool: " + e.getMessage(), e);
            return ToolExecutionResponse.failed(TOOL_NAME, "Error executing CSV processing: " + e.getMessage(), requestId);
        }
    }
    
    @Override
    public CompletableFuture<ToolExecutionResponse> executeAsync(ToolExecutionRequest request) {
        String requestId = UUID.randomUUID().toString();
        LOG.info("Executing CSV processing tool asynchronously, requestId: " + requestId);
        
        CompletableFuture<ToolExecutionResponse> resultFuture = new CompletableFuture<>();
        
        try {
            // Convert from orchestration model to CSV processing model
            CsvProcessingRequest csvRequest = createCsvProcessingRequest(request);
            
            // Process the CSV asynchronously
            csvProcessingService.processAsync(csvRequest);
            
            // Complete the future with an "accepted" response immediately
            resultFuture.complete(ToolExecutionResponse.accepted(TOOL_NAME, requestId));
            
        } catch (Exception e) {
            LOG.error("Error executing CSV processing tool asynchronously: " + e.getMessage(), e);
            resultFuture.complete(
                    ToolExecutionResponse.failed(TOOL_NAME, "Error executing CSV processing: " + e.getMessage(), requestId));
        }
        
        return resultFuture;
    }
    
    @Override
    public String validateParameters(ToolExecutionRequest request) {
        if (request.getParameters() == null) {
            return "Parameters are required";
        }
        
        String phoneNumber = request.getParameterAs("phoneNumber", null);
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return "Phone number is required";
        }
        
        String contentUrl = request.getParameterAs("contentUrl", null);
        if (contentUrl == null || contentUrl.trim().isEmpty()) {
            return "Content URL is required";
        }
        
        String contentType = request.getParameterAs("contentType", null);
        if (contentType == null || contentType.trim().isEmpty()) {
            return "Content type is required";
        }
        
        if (!"document".equals(contentType)) {
            return "Content type must be 'document'";
        }
        
        // Basic URL validation
        if (!contentUrl.startsWith("http://") && !contentUrl.startsWith("https://")) {
            return "Content URL must be a valid HTTP or HTTPS URL";
        }
        
        return null; // Valid
    }
    
    /**
     * Creates a CsvProcessingRequest from a ToolExecutionRequest.
     */
    private CsvProcessingRequest createCsvProcessingRequest(ToolExecutionRequest request) {
        String phoneNumber = request.getParameterAs("phoneNumber", "");
        String documentUrl = request.getParameterAs("contentUrl", "");
        String messageId = request.getParameterAs("messageId", "");
        
        return new CsvProcessingRequest(phoneNumber, documentUrl, messageId);
    }
}
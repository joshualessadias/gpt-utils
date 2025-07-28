package com.joshua.dias.gptutils.csv.service;

import com.joshua.dias.gptutils.csv.model.CsvProcessingRequest;
import com.joshua.dias.gptutils.csv.model.CsvProcessingResponse;

import java.util.concurrent.CompletableFuture;

/**
 * Service interface for CSV processing.
 */
public interface CsvProcessingService {
    
    /**
     * Processes a CSV document synchronously.
     * 
     * @param request The CSV processing request
     * @return The CSV processing response
     */
    CsvProcessingResponse process(CsvProcessingRequest request);
    
    /**
     * Processes a CSV document asynchronously.
     * 
     * @param request The CSV processing request
     * @return A CompletableFuture that will be completed with the CSV processing response
     */
    CompletableFuture<CsvProcessingResponse> processAsync(CsvProcessingRequest request);
}
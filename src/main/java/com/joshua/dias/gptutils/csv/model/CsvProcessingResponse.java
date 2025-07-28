package com.joshua.dias.gptutils.csv.model;

import java.util.List;

/**
 * Response model for CSV processing.
 */
public class CsvProcessingResponse {
    private String phoneNumber;
    private String message;
    private boolean success;
    private List<PropertyDTO> filteredData;

    // Default constructor
    public CsvProcessingResponse() {
    }

    // Constructor for successful processing
    public CsvProcessingResponse(String phoneNumber, List<PropertyDTO> filteredData) {
        this.phoneNumber = phoneNumber;
        this.filteredData = filteredData;
        this.success = true;
        this.message = "CSV processed successfully";
    }

    // Constructor for failed processing
    public CsvProcessingResponse(String phoneNumber, String errorMessage) {
        this.phoneNumber = phoneNumber;
        this.message = errorMessage;
        this.success = false;
        this.filteredData = null;
    }

    // Getters and setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<PropertyDTO> getFilteredData() {
        return filteredData;
    }

    public void setFilteredData(List<PropertyDTO> filteredData) {
        this.filteredData = filteredData;
    }
}
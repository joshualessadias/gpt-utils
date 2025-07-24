package com.joshua.dias.gptutils.transcription.service;

import com.joshua.dias.gptutils.transcription.model.TranscriptionRequest;
import com.joshua.dias.gptutils.transcription.model.TranscriptionResponse;

/**
 * Service interface for the transcription workflow.
 * Following the Interface Segregation Principle, this interface focuses solely on the workflow.
 */
public interface TranscriptionWorkflowService {
    
    /**
     * Processes an audio transcription request through the defined workflow.
     *
     * @param request The transcription request containing the audio data and metadata
     * @return A response containing the transcribed text or error information
     */
    TranscriptionResponse processTranscription(TranscriptionRequest request);
}
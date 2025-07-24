package com.joshua.dias.gptutils.transcription.service;

import com.joshua.dias.gptutils.transcription.model.TranscriptionRequest;
import com.joshua.dias.gptutils.transcription.model.TranscriptionResponse;

/**
 * Service interface for audio transcription.
 * Following the Interface Segregation Principle, this interface focuses solely on transcription.
 */
public interface TranscriptionService {
    
    /**
     * Transcribes audio data to text.
     *
     * @param request The transcription request containing the audio data and metadata
     * @return A response containing the transcribed text or error information
     */
    TranscriptionResponse transcribe(TranscriptionRequest request);
}
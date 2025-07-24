package com.joshua.dias.gptutils.transcription.service;

import com.joshua.dias.gptutils.transcription.model.TranscriptionRequest;
import com.joshua.dias.gptutils.transcription.model.TranscriptionResponse;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.time.Duration;

/**
 * Implementation of TranscriptionWorkflowService using LangChain4j.
 * This class defines and executes the workflow for processing audio transcription.
 */
@ApplicationScoped
public class LangChainTranscriptionWorkflowService implements TranscriptionWorkflowService {

    private static final Logger LOG = Logger.getLogger(LangChainTranscriptionWorkflowService.class);

    private final TranscriptionService transcriptionService;
    private final TranscriptionValidator validator;
    private final TranscriptionWorkflow workflow;

    /**
     * Constructor that initializes the workflow with dependencies.
     */
    @Inject
    public LangChainTranscriptionWorkflowService(
            TranscriptionService transcriptionService,
            @ConfigProperty(name = "openai.api-key") String apiKey,
            @ConfigProperty(name = "openai.chat-model") String chatModel,
            @ConfigProperty(name = "openai.timeout", defaultValue = "30") Integer timeout
    ) {
        this.transcriptionService = transcriptionService;
        this.validator = new TranscriptionValidator();

        // Initialize the LangChain workflow
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(chatModel)
                .timeout(Duration.ofSeconds(timeout))
                .build();

        this.workflow = AiServices.builder(TranscriptionWorkflow.class)
                .chatModel(model)
                .chatMemory(MessageWindowChatMemory.builder().maxMessages(10).build())
                .build();

        LOG.info("LangChainTranscriptionWorkflowService initialized with chat model: " + chatModel);
    }

    @Override
    public TranscriptionResponse processTranscription(TranscriptionRequest request) {
        
        try {
            // Step 1: Validate the request
            String validationError = validator.validate(request);
            if (validationError != null) {
                LOG.warn("Validation error: " + validationError);
                return new TranscriptionResponse(request.getPhoneNumber(), validationError, false);
            }

            // Step 2: Transcribe the audio
            LOG.info("Transcribing audio for phone number: " + request.getPhoneNumber());
            TranscriptionResponse transcriptionResponse = transcriptionService.transcribe(request);

            // If transcription failed, return the error
            if (!transcriptionResponse.isSuccess()) {
                return transcriptionResponse;
            }

            // Step 3: Process the transcription using the LangChain workflow with GPT-4.1 Nano
            String transcribedText = transcriptionResponse.getTranscribedText();
            LOG.info("Processing transcription with LangChain workflow");
            LOG.debug("Transcribed text to be processed: " + (transcribedText != null ? transcribedText.substring(0, Math.min(100, transcribedText.length())) + "..." : "null"));

            String processedText = workflow.processTranscription(transcribedText);
            LOG.debug("Processed text result: " + (processedText != null ? processedText.substring(0, Math.min(100, processedText.length())) + "..." : "null"));

            // Return the processed response
            return new TranscriptionResponse(request.getPhoneNumber(), processedText);

        } catch (Exception e) {
            LOG.error("Error in transcription workflow: " + e.getMessage(), e);
            return new TranscriptionResponse(request.getPhoneNumber(),
                    "Error in transcription workflow: " + e.getMessage(), false);
        }
    }

    /**
     * Interface for the LangChain workflow steps.
     * This is used to define the workflow using LangChain4j's AiServices.
     */
    private interface TranscriptionWorkflow {
        @SystemMessage("""
                You are an expert transcription processor acting as an intermediary between a sender and a receiver. Your task is to:

                1. Respond in the same language as the transcription (do not translate).
                2. Correct any transcription errors or inconsistencies.
                3. Improve grammar and punctuation.
                4. Format the text for better readability.
                5. Preserve the original meaning and intent.
                6. If the transcription is empty, respond with: "Transcription is empty. No processing can be done."
                7. Do not mention errors or processing steps in the final output.

                At the end, include a compact and simple summary with two parts:
                    – Intent: What the sender wants or means.
                    – Response Suggestion: How the receiver should respond.
 
                8. Follow the format (nothing else):
                    [transcribedText]

                    *[intent_title]*: [intent]

                    *[response_suggestion_title]*: [response]

                Keep both parts clear and concise.
            """)
        @UserMessage("Process this audio transcription: {{transcribedText}}")
        String processTranscription(@V("transcribedText") String transcribedText);
    }

    /**
     * Inner class for validating transcription requests.
     */
    private static class TranscriptionValidator {

        /**
         * Validates a transcription request.
         *
         * @param request The request to validate
         * @return null if valid, error message if invalid
         */
        public String validate(TranscriptionRequest request) {
            if (request == null) {
                return "Request cannot be null";
            }

            if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
                return "Phone number is required";
            }

            if (request.getAudioUrl() == null || request.getAudioUrl().trim().isEmpty()) {
                return "Audio URL is required";
            }

            // Basic URL validation
            if (!request.getAudioUrl().startsWith("http://") && !request.getAudioUrl().startsWith("https://")) {
                return "Audio URL must be a valid HTTP or HTTPS URL";
            }

            return null; // Valid
        }
    }
}
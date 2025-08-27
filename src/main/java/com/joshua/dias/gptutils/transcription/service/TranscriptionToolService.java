package com.joshua.dias.gptutils.transcription.service;

import com.joshua.dias.gptutils.transcription.model.TranscriptionRequest;
import com.joshua.dias.gptutils.transcription.model.TranscriptionResponse;
import com.joshua.dias.gptutils.zapi.service.ZApiService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Service for asynchronous transcription processing.
 * This service processes transcription requests in the background and sends notifications when complete.
 */
@ApplicationScoped
public class TranscriptionToolService {

    private static final Logger LOG = Logger.getLogger(TranscriptionToolService.class);

    private final TranscriptionWorkflowService workflowService;
    private final NotificationService notificationService;
    private final ZApiService zApiService;
    private final int corePoolSize;
    private final int maxPoolSize;
    private final int queueSize;
    private final int retryAttempts;
    private final int retryDelay;
    private ExecutorService executorService;

    /**
     * Constructor that injects dependencies and configuration.
     */
    @Inject
    public TranscriptionToolService(
            TranscriptionWorkflowService workflowService,
            NotificationService notificationService,
            ZApiService zApiService,
            @ConfigProperty(name = "app.async.core-pool-size", defaultValue = "5") int corePoolSize,
            @ConfigProperty(name = "app.async.max-pool-size", defaultValue = "10") int maxPoolSize,
            @ConfigProperty(name = "app.async.queue-size", defaultValue = "100") int queueSize,
            @ConfigProperty(name = "app.notification.retry-attempts", defaultValue = "3") int retryAttempts,
            @ConfigProperty(name = "app.notification.retry-delay", defaultValue = "1000") int retryDelay
    ) {
        this.workflowService = workflowService;
        this.notificationService = notificationService;
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.queueSize = queueSize;
        this.retryAttempts = retryAttempts;
        this.retryDelay = retryDelay;
        this.zApiService = zApiService;
    }

    /**
     * Initializes the thread pool.
     */
    @PostConstruct
    void init() {
        LOG.info("Initializing AsyncTranscriptionService with core pool size: " + corePoolSize +
                ", max pool size: " + maxPoolSize + ", queue size: " + queueSize);

        // Create a thread pool with bounded queue
        this.executorService = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueSize),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy() // If queue is full, caller thread executes the task
        );
    }

    /**
     * Shuts down the thread pool.
     */
    @PreDestroy
    void shutdown() {
        LOG.info("Shutting down AsyncTranscriptionService");
        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Processes a transcription request asynchronously.
     *
     * @param request The transcription request
     */
    public void processAsync(TranscriptionRequest request) {
        LOG.info("Submitting async transcription request for phone: " + request.getPhoneNumber());

        CompletableFuture.supplyAsync(() -> process(request), executorService);
    }

    public TranscriptionResponse process(TranscriptionRequest request) {
        zApiService.readMessage(request.getPhoneNumber(), request.getMessageId());

        try {
            // Process the transcription
            LOG.info("Processing transcription for phone: " + request.getPhoneNumber());
            TranscriptionResponse response = workflowService.processTranscription(request);

            // Send notification with retry
            sendNotificationWithRetry(response);

            return response;
        } catch (Exception e) {
            LOG.error("Error in async transcription processing: " + e.getMessage(), e);
            return new TranscriptionResponse(request.getPhoneNumber(),
                    "Error in async transcription processing: " + e.getMessage(), false);
        }
    }

    /**
     * Sends a notification with retry logic.
     *
     * @param response The transcription response
     */
    private void sendNotificationWithRetry(TranscriptionResponse response) {
        int attempts = 0;
        boolean success = false;

        while (!success && attempts < retryAttempts) {
            attempts++;
            try {
                LOG.info("Sending notification, attempt " + attempts + " for phone: " + response.getPhoneNumber());
                success = notificationService.sendNotification(response);

                if (success) {
                    LOG.info("Notification sent successfully after " + attempts + " attempts");
                } else if (attempts < retryAttempts) {
                    LOG.warn("Notification failed, will retry in " + retryDelay + "ms");
                    Thread.sleep(retryDelay);
                }
            } catch (Exception e) {
                LOG.error("Error sending notification (attempt " + attempts + "): " + e.getMessage(), e);
                if (attempts < retryAttempts) {
                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }

        if (!success) {
            LOG.error("Failed to send notification after " + attempts + " attempts for phone: " +
                    response.getPhoneNumber());
        }
    }
}
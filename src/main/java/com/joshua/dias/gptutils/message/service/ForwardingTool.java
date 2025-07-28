package com.joshua.dias.gptutils.message.service;

import com.joshua.dias.gptutils.orchestration.model.ToolExecutionRequest;
import com.joshua.dias.gptutils.orchestration.model.ToolExecutionResponse;
import com.joshua.dias.gptutils.orchestration.service.Tool;
import com.joshua.dias.gptutils.zapi.service.ZApiService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Tool for forwarding messages to a specific phone number.
 * This tool is used as a fallback when a message is not mapped to any other tool.
 */
@ApplicationScoped
public class ForwardingTool implements Tool {

    private static final Logger LOG = Logger.getLogger(ForwardingTool.class);
    private static final String TOOL_NAME = "forwarding";
    private static final String TOOL_DESCRIPTION = "Forwards messages to a specific phone number";

    // The phone number to forward messages to
    private static final String FORWARD_TO_PHONE = "120363419205372574-group";

    private final ZApiService zApiService;

    /**
     * Constructor that injects dependencies.
     */
    @Inject
    public ForwardingTool(ZApiService zApiService) {
        this.zApiService = zApiService;
        LOG.info("ForwardingTool initialized");
    }

    @Override
    public String getName() {
        return TOOL_NAME;
    }

    @Override
    public String getDescription() {
        return TOOL_DESCRIPTION;
    }

    @Override
    public ToolExecutionResponse execute(ToolExecutionRequest request) {
        String requestId = request.getParameterAs("requestId", "unknown");
        LOG.info("Executing ForwardingTool, requestId: " + requestId);

        try {
            // Extract parameters
            String senderPhone = request.getParameterAs("phoneNumber", "");
            String messageContent = request.getParameterAs("messageContent", "");
            String messageType = request.getParameterAs("messageType", "");
            String senderName = request.getParameterAs("senderName", "Unknown");
            String messageId = request.getParameterAs("messageId", "");
            if (senderPhone.isEmpty() || messageContent.isEmpty()) {
                return ToolExecutionResponse.rejected(
                        TOOL_NAME,
                        "Missing required parameters: phoneNumber and messageContent",
                        requestId
                );
            }

            // Forward the original message
            String forwardedMessageId = zApiService.forwardMessage(
                    FORWARD_TO_PHONE,
                    messageId,
                    senderPhone
            );

            if (forwardedMessageId == null) {
                return ToolExecutionResponse.failed(
                        TOOL_NAME,
                        "Failed to forward message",
                        requestId
                );
            }

            // Send a reference message with sender information
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            var referenceMessageSb = new StringBuilder()
                    .append("Message forwarded from:\n")
                    .append("- Phone: ").append(senderPhone).append("\n")
                    .append("- Name: ").append(senderName).append("\n");
            if (!messageType.isEmpty())
                referenceMessageSb.append("- Type: ").append(messageType).append("\n");
            referenceMessageSb.append("- Time: ").append(timestamp);

            boolean referenceSuccess = zApiService.sendMessage(
                    FORWARD_TO_PHONE,
                    referenceMessageSb.toString(),
                    forwardedMessageId
            );

            if (!referenceSuccess) {
                LOG.warn("Failed to send reference message");
            }

            // Create response
            Map<String, Object> result = new HashMap<>();
            result.put("forwarded", true);
            result.put("forwardedTo", FORWARD_TO_PHONE);
            result.put("forwardedMessageId", forwardedMessageId);
            result.put("referenceSent", referenceSuccess);

            return ToolExecutionResponse.completed(TOOL_NAME, result, requestId);

        } catch (Exception e) {
            LOG.error("Error executing ForwardingTool: " + e.getMessage(), e);
            return ToolExecutionResponse.failed(
                    TOOL_NAME,
                    "Error executing ForwardingTool: " + e.getMessage(),
                    requestId
            );
        }
    }

    @Override
    public CompletableFuture<ToolExecutionResponse> executeAsync(ToolExecutionRequest request) {
        return CompletableFuture.supplyAsync(() -> execute(request));
    }

    @Override
    public String validateParameters(ToolExecutionRequest request) {
        String senderPhone = request.getParameterAs("phoneNumber", "");
        String messageContent = request.getParameterAs("messageContent", "");
        String messageId = request.getParameterAs("messageId", "");

        if (senderPhone.isEmpty()) {
            return "Missing required parameter: phoneNumber";
        }

        if (messageContent.isEmpty()) {
            return "Missing required parameter: messageContent";
        }

        if (messageId.isEmpty()) {
            return "Missing required parameter: messageId";
        }

        return null; // Parameters are valid
    }
}
package com.joshua.dias.gptutils.commom.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.logging.Logger;

@Provider
public class LoggingFilter implements ClientRequestFilter {

    private final ObjectMapper objectMapper;
    private final boolean filterEnabled;

    private final Logger log = Logger.getLogger(getClass().getName());

    @Inject
    public LoggingFilter(
            ObjectMapper objectMapper,
            @ConfigProperty(name = "app.logging.filter-enabled", defaultValue = "false") boolean filterEnabled
    ) {
        this.objectMapper = objectMapper;
        this.filterEnabled = filterEnabled;
    }

    @Override
    public void filter(ClientRequestContext requestContext) {
        if (!filterEnabled) {
            return; // Skip logging if filter is not enabled
        }

        log.info("Request URL: " + requestContext.getUri());
        log.info("Method: " + requestContext.getMethod());
        log.info("Headers: " + requestContext.getHeaders());

        if (requestContext.hasEntity()) {
            try {
                // Convert entity to JSON string using ObjectMapper
                Object entity = requestContext.getEntity();
                String jsonEntity = objectMapper.writeValueAsString(entity);
                log.info("Body: " + jsonEntity);
            } catch (JsonProcessingException e) {
                // Fallback to toString() if JSON conversion fails
                log.warning("Failed to convert entity to JSON: " + e.getMessage());
                log.info("Body (toString): " + requestContext.getEntity());
            }
        }
    }
}
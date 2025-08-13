package com.joshua.dias.gptutils.zapi.client;

import com.joshua.dias.gptutils.zapi.model.ForwardMessageRequestDTO;
import com.joshua.dias.gptutils.zapi.model.ReadMessageRequestDTO;
import com.joshua.dias.gptutils.zapi.model.SendMessageRequestDTO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * REST client interface for the Z-API endpoint.
 * This will be called to send WhatsApp messages via Z-API.
 */
@RegisterRestClient(configKey = "zapi-client")
@ClientHeaderParam(name = "Client-Token", value = "{getClientToken}")
public interface ZApiClient {

    default String getClientToken() {
        return ConfigProvider.getConfig().getValue("zapi.client-token", String.class);
    }

    /**
     * Sends a WhatsApp message via Z-API.
     *
     * @param request The message request containing phone number and message content
     * @return HTTP response from the Z-API endpoint
     */
    @POST
    @Path("/send-text")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response sendMessage(SendMessageRequestDTO request);
    
    /**
     * Forwards a WhatsApp message via Z-API.
     *
     * @param request The forward message request containing target phone number and message ID to forward
     * @return HTTP response from the Z-API endpoint
     */
    @POST
    @Path("/forward-message")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response forwardMessage(ForwardMessageRequestDTO request);
    
    /**
     * Marks a WhatsApp message as read via Z-API.
     *
     * @param request The read message request containing phone number and message ID to mark as read
     * @return HTTP response from the Z-API endpoint
     */
    @POST
    @Path("/read-message")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response readMessage(ReadMessageRequestDTO request);
}
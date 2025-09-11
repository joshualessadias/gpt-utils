package com.joshua.dias.gptutils.confirmaai;

import com.joshua.dias.gptutils.message.model.ReceiveMessageDTO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "confirmaai-client")
public interface ConfirmaAiClient {

    @POST
    @Path("/webhook")
    @Consumes("application/json")
    @Produces("application/json")
    Response triggerWebhook(ReceiveMessageDTO request);
}

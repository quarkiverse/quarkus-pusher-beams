package io.quarkiverse.pusher.beams.it;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import io.quarkiverse.pusher.beams.notification.PublishRequest;
import io.quarkiverse.pusher.beams.server.BeamsClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/pusher-beams")
@ApplicationScoped
public class PusherBeamsResource {

    @Inject
    BeamsClient beamsClient;

    @GET()
    @Path("/active")
    public boolean isActive() {

        return null != beamsClient;
    }

    @GET()
    @Path("/token")
    public boolean token() {

        return !beamsClient.generateToken(UUID.randomUUID()
                .toString())
                .isEmpty();
    }

    @GET()
    @Path("/publish")
    @Produces(MediaType.TEXT_PLAIN)
    public boolean publish() throws IOException, InterruptedException, URISyntaxException {

        PublishRequest request = new PublishRequest();
        request.apns()
                .pusher()
                .withDisableDeliveryTracking();
        request.apns()
                .aps()
                .alert()
                .withTitle("test");

        return null != beamsClient.publishToUsers(List.of(UUID.randomUUID()
                .toString()), request);
    }

}

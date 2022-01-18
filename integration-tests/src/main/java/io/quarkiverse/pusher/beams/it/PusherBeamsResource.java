package io.quarkiverse.pusher.beams.it;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkiverse.pusher.beams.notification.PublishRequest;
import io.quarkiverse.pusher.beams.server.BeamsClient;

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

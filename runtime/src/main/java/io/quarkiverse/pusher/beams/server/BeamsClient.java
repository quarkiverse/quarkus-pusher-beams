package io.quarkiverse.pusher.beams.server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import com.pusher.pushnotifications.PushNotifications;

import io.quarkiverse.pusher.beams.server.retry.RateLimited;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * This class acts as a proxy to access and use the {@link PushNotifications} object from Pusher
 * Beams library. We do this for two reasons: <br>
 * - the API methods of the {@link PushNotifications} beans are final on the library and therefore
 * cannot be proxified <br>
 * - having this proxy enable us to handle the retry logic for 429 error through the
 * {@link RateLimited} interceptor
 */
@ApplicationScoped
public class BeamsClient {

    @Inject
    PushNotificationsFactory factory;

    PushNotifications pushNotifications;

    @PostConstruct
    public void setup() {
        this.pushNotifications = factory.generate();
    }

    @RateLimited
    public boolean deleteUser(final String userId) {

        pushNotifications.deleteUser(userId);
        return Boolean.TRUE;
    }

    public Map<String, Object> generateToken(final String userId) {

        return pushNotifications.generateToken(userId);
    }

    @RateLimited
    public String publishToUsers(final List<String> users, final Map<String, Object> publishData)
            throws IOException, InterruptedException, URISyntaxException {

        return pushNotifications.publishToUsers(users, publishData);
    }

    @RateLimited
    public String publishToInterests(final List<String> users,
            final Map<String, Object> publishData)
            throws IOException, InterruptedException, URISyntaxException {

        return pushNotifications.publishToInterests(users, publishData);
    }

}

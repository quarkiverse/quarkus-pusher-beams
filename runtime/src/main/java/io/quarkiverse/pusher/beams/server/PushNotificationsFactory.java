package io.quarkiverse.pusher.beams.server;

import com.pusher.pushnotifications.PushNotifications;

import io.quarkiverse.pusher.beams.config.BeamsConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PushNotificationsFactory {

    @Inject
    BeamsConfig config;

    public PushNotifications generate() {
        return new PushNotifications(config.instanceId, config.secretKey);
    }
}

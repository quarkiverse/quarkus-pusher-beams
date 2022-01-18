package io.quarkiverse.pusher.beams.server;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.pusher.pushnotifications.PushNotifications;

import io.quarkiverse.pusher.beams.config.BeamsConfig;

@ApplicationScoped
public class PushNotificationsFactory {

    @Inject
    BeamsConfig config;

    public PushNotifications generate() {
        return new PushNotifications(config.instanceId, config.secretKey);
    }
}

package io.quarkiverse.pusher.beams.notification;

import java.util.HashMap;

import io.quarkiverse.pusher.beams.notification.apple.Apns;

/**
 * Skeleton of publish information to submit notification through Pusher Beams. Feel free to use
 * this class, complete it with PR if you need additional options or the Android/Web structure as
 * well. You are free to implement your own since we kept the API as Map to publish to users.
 */
public class PublishRequest extends HashMap<String, Object> {
    /** Default serial ID */
    private static final long serialVersionUID = 1L;
    private static final String KEY_SYSTEM_APNS = "apns";

    public PublishRequest() {
        put(KEY_SYSTEM_APNS, new Apns());
    }

    public Apns apns() {
        return (Apns) get(KEY_SYSTEM_APNS);
    }
}

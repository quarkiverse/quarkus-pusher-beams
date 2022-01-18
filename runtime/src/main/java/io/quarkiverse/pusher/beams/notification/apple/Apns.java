package io.quarkiverse.pusher.beams.notification.apple;

import java.util.HashMap;

import io.quarkiverse.pusher.beams.notification.common.PusherOptions;

public class Apns extends HashMap<String, Object> {
    /** Default serial ID */
    private static final long serialVersionUID = 1L;
    private static final String KEY_SYSTEM_APS = "aps";
    private static final String KEY_SYSTEM_PUSHER = "pusher";

    public Apns() {
        put(KEY_SYSTEM_APS, new Aps());
        put(KEY_SYSTEM_PUSHER, new PusherOptions());
    }

    public Aps aps() {
        return (Aps) get(KEY_SYSTEM_APS);
    }

    public PusherOptions pusher() {
        return (PusherOptions) get(KEY_SYSTEM_PUSHER);
    }
}

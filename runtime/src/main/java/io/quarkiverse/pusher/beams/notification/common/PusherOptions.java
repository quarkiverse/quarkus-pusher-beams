package io.quarkiverse.pusher.beams.notification.common;

import java.util.HashMap;

public class PusherOptions extends HashMap<String, Object> {
    /** Default serial ID */
    private static final long serialVersionUID = 1L;

    private static final String KEY_DISABLE_DELIVERY_TRACKING = "disable_delivery_tracking";

    public PusherOptions withDisableDeliveryTracking() {
        put(KEY_DISABLE_DELIVERY_TRACKING, true);
        return this;
    }
}

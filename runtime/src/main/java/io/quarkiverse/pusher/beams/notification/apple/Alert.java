package io.quarkiverse.pusher.beams.notification.apple;

import java.util.HashMap;

public class Alert extends HashMap<String, String> {
    /** Default serial ID */
    private static final long serialVersionUID = 1L;
    private static final String KEY_TITLE = "title";
    private static final String KEY_SUBTITLE = "subtitle";
    private static final String KEY_BODY = "body";

    public Alert withTitle(final String title) {
        put(KEY_TITLE, title);
        return this;
    }

    public Alert withSubtitle(final String subtitle) {
        put(KEY_SUBTITLE, subtitle);
        return this;
    }

    public Alert withBody(final String body) {
        put(KEY_BODY, body);
        return this;
    }
}

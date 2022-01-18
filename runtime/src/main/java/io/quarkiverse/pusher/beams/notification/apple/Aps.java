package io.quarkiverse.pusher.beams.notification.apple;

import java.util.HashMap;

public class Aps extends HashMap<String, Object> {
    /** Default serial ID */
    private static final long serialVersionUID = 1L;
    private static final String SOUND_DEFAULT = "default";

    private static final String KEY_ALERT = "alert";
    private static final String KEY_BADGE = "badge";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_SOUND = "sound";
    private static final String KEY_MUTABLE_CONTENT = "mutable-content";
    private static final Integer MUTABLE_CONTENT_VALUE = 1;

    public Aps() {
        put(KEY_ALERT, new Alert());
        put(KEY_SOUND, SOUND_DEFAULT);
    }

    public Alert alert() {
        return (Alert) get(KEY_ALERT);
    }

    public Aps withBadge(final int number) {

        if (number < 0) {
            // Ignore negative numbers
            return this;
        }

        put(KEY_BADGE, Integer.valueOf(number));
        return this;
    }

    public Aps withSound(final String sound) {
        if (null != sound) {
            put(KEY_SOUND, sound);
        }
        return this;
    }

    public Aps withCategory(final String category) {
        if (null != category) {
            put(KEY_CATEGORY, category);
        }
        return this;
    }

    public Aps withMutableContent() {

        put(KEY_MUTABLE_CONTENT, MUTABLE_CONTENT_VALUE);
        return this;
    }
}

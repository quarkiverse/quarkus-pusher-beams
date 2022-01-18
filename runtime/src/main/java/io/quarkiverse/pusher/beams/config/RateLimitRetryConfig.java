package io.quarkiverse.pusher.beams.config;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

/**
 * Rate limit configurations
 * <p>
 * Since Pusher has rate limits on its API, this extension offer a retry capability to automatically
 * re submit your request after some time
 */
@ConfigGroup
public class RateLimitRetryConfig {

    /**
     * The maximum number of retry when a 429 is returned by Pusher API.
     */
    @ConfigItem(defaultValue = "5")
    public int maxRetry;

    /**
     * The time to wait between attempts, in milliseconds.
     */
    @ConfigItem(defaultValue = "20")
    public int delay;
}

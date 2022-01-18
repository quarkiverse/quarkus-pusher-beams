package io.quarkiverse.pusher.beams.config;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "pusher.beams", phase = ConfigPhase.RUN_TIME)
public class BeamsConfig {
    /**
     * The Pusher beams instance identifier.
     */
    @ConfigItem
    public String instanceId;
    /**
     * The Pusher beams instance secret.
     */
    @ConfigItem
    public String secretKey;
    /**
     * The rate limit retry configuration.
     */
    @ConfigItem
    public RateLimitRetryConfig rateLimit;
}

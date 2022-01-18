package io.quarkiverse.pusher.beams.config;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "pusher.beams", phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
public class BeamsBuildConfig {
    /**
     * Pusher beams server SDK support
     * <p>
     * Pusher beams server SDK support is enabled by default.
     */
    @ConfigItem(defaultValue = "true")
    public boolean enabled;
}

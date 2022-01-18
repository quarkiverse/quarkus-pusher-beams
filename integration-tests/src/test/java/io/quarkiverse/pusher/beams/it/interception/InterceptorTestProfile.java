package io.quarkiverse.pusher.beams.it.interception;

import java.util.HashMap;
import java.util.Map;

import io.quarkus.test.junit.QuarkusTestProfile;

public class InterceptorTestProfile implements QuarkusTestProfile {
    private static final Map<String, String> CONFIGURATION = new HashMap<String, String>();

    @Override
    public Map<String, String> getConfigOverrides() {
        return CONFIGURATION;
    }

    @Override
    public String getConfigProfile() {
        return "interceptor-test";
    }

}

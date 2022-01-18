package io.quarkiverse.pusher.beams;

import java.util.function.BooleanSupplier;

import javax.inject.Inject;

import com.pusher.pushnotifications.PublishNotificationResponse;
import com.pusher.pushnotifications.PushNotificationErrorResponse;

import io.quarkiverse.pusher.beams.config.BeamsBuildConfig;
import io.quarkiverse.pusher.beams.server.BeamsClient;
import io.quarkiverse.pusher.beams.server.PushNotificationsFactory;
import io.quarkiverse.pusher.beams.server.retry.RateLimitedInterceptor;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;

public class BeamsProcessor {

    private static final String FEATURE = "pusher-beams";

    public static class BeamsServerEnabled implements BooleanSupplier {

        BeamsBuildConfig buildConfig;

        @Override
        public boolean getAsBoolean() {
            return buildConfig.enabled;
        }
    }

    @Inject
    CombinedIndexBuildItem combinedIndexBuildItem;

    @BuildStep(onlyIf = BeamsServerEnabled.class)
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep(onlyIf = BeamsServerEnabled.class)
    void registerReflectionForPusherClasses(//
            BuildProducer<ReflectiveClassBuildItem> reflectiveClass) {

        reflectiveClass.produce(
                new ReflectiveClassBuildItem(true, true, PublishNotificationResponse.class));
        reflectiveClass.produce(
                new ReflectiveClassBuildItem(true, true, PushNotificationErrorResponse.class));
    }

    @BuildStep(onlyIf = BeamsServerEnabled.class)
    void registerExtensionBeans(BuildProducer<AdditionalBeanBuildItem> additionalBeans) {

        additionalBeans
                .produce(AdditionalBeanBuildItem.unremovableOf(RateLimitedInterceptor.class));
        additionalBeans
                .produce(AdditionalBeanBuildItem.unremovableOf(PushNotificationsFactory.class));
        additionalBeans.produce(AdditionalBeanBuildItem.unremovableOf(BeamsClient.class));
    }
}

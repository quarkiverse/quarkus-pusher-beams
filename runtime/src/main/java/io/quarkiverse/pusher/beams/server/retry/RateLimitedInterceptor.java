package io.quarkiverse.pusher.beams.server.retry;

import java.text.MessageFormat;

import org.jboss.logging.Logger;

import com.pusher.pushnotifications.PusherTooManyRequestsError;

import io.quarkiverse.pusher.beams.config.BeamsConfig;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

/**
 * This interceptor is responsible of handling {@link PusherTooManyRequestsError} exception around
 * Pusher Beams SDK calls and applies a retry mechanism to automatically re submit the SDK request
 * after a short configurable time.
 */
@RateLimited
@Priority(10)
@Interceptor
public class RateLimitedInterceptor {
    private static final Logger LOGGER = Logger.getLogger(RateLimitedInterceptor.class.getName());

    private static final String OPERATION_EXECUTION_FAILURE = "Too many attempts to execute pusher action {0}";
    private static final String SLEEP_LOG_TEMPLATE = "429 received. Sleeping for {0}";

    @Inject
    BeamsConfig config;

    @AroundInvoke
    Object around(InvocationContext context) throws Throwable {

        final String operationName = context.getMethod()
                .getName();

        LOGGER.debug("Intercepted method: " + operationName);

        final int maxRetry = config.rateLimit.maxRetry;
        final int delay = config.rateLimit.delay;

        int retryCounter = 1;
        PusherTooManyRequestsError lastError = null;
        do {

            try {

                return context.proceed();

            } catch (PusherTooManyRequestsError e) {

                lastError = e;
                sleepBetween429(delay);
            }

        } while (retryCounter++ < maxRetry);

        LOGGER.error(MessageFormat.format(OPERATION_EXECUTION_FAILURE, operationName));

        // At this point, we reached the maximum number of retry so we just throw the last caught
        // exception back to the user
        throw lastError;
    }

    private void sleepBetween429(final int delayInMillis) {
        try {
            LOGGER.info(MessageFormat.format(SLEEP_LOG_TEMPLATE, delayInMillis));
            Thread.sleep(delayInMillis);
        } catch (InterruptedException e) {
            // Ignore;
        }
    }
}

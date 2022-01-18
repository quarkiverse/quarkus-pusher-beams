package io.quarkiverse.pusher.beams.server.retry;

import java.lang.reflect.Method;

import javax.interceptor.InvocationContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pusher.pushnotifications.PusherTooManyRequestsError;

import io.quarkiverse.pusher.beams.config.BeamsConfig;
import io.quarkiverse.pusher.beams.config.RateLimitRetryConfig;

@ExtendWith(MockitoExtension.class)
public class RateLimitedInterceptorTest {
    private static final Object DEFAULT_RETURNED_OBJECT = new Object();

    @Mock
    BeamsConfig mockedConfig;

    @Mock
    InvocationContext mockedContext;

    @Mock
    RateLimitRetryConfig mockedRateLimitConfig;

    @InjectMocks
    RateLimitedInterceptor interceptor = new RateLimitedInterceptor();

    @BeforeEach
    public void setup() throws Exception {

        Method mockedMethod = Mockito.mock(Method.class);
        Mockito.when(mockedContext.getMethod())
                .thenReturn(mockedMethod);
        Mockito.when(mockedMethod.getName())
                .thenReturn("testMethod");

        mockedConfig.rateLimit = mockedRateLimitConfig;
        mockedRateLimitConfig.delay = 10;
        mockedRateLimitConfig.maxRetry = 1;

        Mockito.when(mockedContext.proceed())
                .thenReturn(DEFAULT_RETURNED_OBJECT);
    }

    @Test
    public void around_normalCall_shouldProceed_onlyOnce() throws Throwable {

        Assertions.assertSame(DEFAULT_RETURNED_OBJECT, interceptor.around(mockedContext));

        Mockito.verify(mockedContext, Mockito.times(1))
                .proceed();
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4 })
    public void around_withCallGeneratingPusherTooManyRequestsError_should_retryUpToMaxRetryConfig(
            final int maxRetry) throws Throwable {

        // Given - a retry of xx and a PusherTooManyRequestsError exception thrown
        mockedRateLimitConfig.maxRetry = maxRetry;
        Mockito.when(mockedContext.proceed())
                .thenThrow(new PusherTooManyRequestsError("test case"));

        try {
            // when - invoking
            interceptor.around(mockedContext);
            Assertions.fail(
                    "Expecting the PusherTooManyRequestsError exception to be rethrown after retry attempts");
        } catch (PusherTooManyRequestsError e) {

            // then - the PusherTooManyRequestsError exception should be re-thrown and the
            // interceptor should have retried
            // up xx times
            Mockito.verify(mockedContext, Mockito.times(maxRetry))
                    .proceed();
        }
    }

    @Test
    public void around_withOnlyOnePusherTooManyRequestsError_should_retryOnlyOnce()
            throws Throwable {

        mockedRateLimitConfig.maxRetry = 3;
        Mockito.when(mockedContext.proceed())
                .thenThrow(new PusherTooManyRequestsError("testing"))
                .thenReturn(DEFAULT_RETURNED_OBJECT);

        // Then - the interceptor should have retried only once & then proceed correctly
        Assertions.assertSame(DEFAULT_RETURNED_OBJECT, interceptor.around(mockedContext));
        Mockito.verify(mockedContext, Mockito.times(2))
                .proceed();
    }

    @Test
    public void around_withCallGeneratingAnotherException_shouldNot_retry() throws Throwable {

        // Given - a retry of 3 and a catchable exception thrown
        mockedRateLimitConfig.maxRetry = 3;
        Mockito.when(mockedContext.proceed())
                .thenThrow(new CatchableException());

        try {
            // when - invoking
            interceptor.around(mockedContext);
            Assertions.fail("Expecting CatchableException to be thrown");
        } catch (CatchableException e) {

            // then - the expected exception should be thrown and the interceptor should not retry
            // to proceed the call
            Mockito.verify(mockedContext, Mockito.times(1))
                    .proceed();
        }
    }

    public static class CatchableException extends RuntimeException {
        /** Default serial ID */
        private static final long serialVersionUID = 1L;

    }
}

package io.quarkiverse.pusher.beams.it.interception;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.pusher.pushnotifications.PushNotifications;
import com.pusher.pushnotifications.PusherTooManyRequestsError;

import io.quarkiverse.pusher.beams.config.BeamsConfig;
import io.quarkiverse.pusher.beams.server.BeamsClient;
import io.quarkiverse.pusher.beams.server.PushNotificationsFactory;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
// Keep this test execution separated from the other
// tests since it's mocking the pusher behavior
@TestProfile(InterceptorTestProfile.class)
public class BeamsClientInterceptionTest {

    @Inject
    BeamsConfig config;

    @InjectMock
    PushNotificationsFactory mockedFactory;

    static PushNotifications mockedPushNotifications;

    @Inject
    BeamsClient beamsClient;

    @BeforeAll
    public static void globalSetup() {

        mockedPushNotifications = Mockito.mock(PushNotifications.class);
    }

    @BeforeEach
    public void setup() {

        Mockito.when(mockedFactory.generate())
                .thenReturn(mockedPushNotifications);
    }

    @Test
    public void rateLimitedInterceptor_shouldHandle_deleteUser_operation() throws Throwable {

        // Given
        final int expectedAttempts = config.rateLimit.maxRetry;
        final String userId = "test";
        Mockito.doThrow(new PusherTooManyRequestsError("test"))
                .when(mockedPushNotifications)
                .deleteUser(Mockito.anyString());

        try {

            // When
            beamsClient.deleteUser(userId);
            Assertions.fail("Expecting a PusherTooManyRequestsError to be thrown for this test");

        } catch (PusherTooManyRequestsError e) {

            // Then
            Mockito.verify(mockedPushNotifications, Mockito.times(expectedAttempts))
                    .deleteUser(userId);
        }
    }

    @Test
    public void rateLimitedInterceptor_shouldNotHandle_generateToken_operation() throws Throwable {

        // Given
        final int expectedAttempts = 1;
        final String userId = "test";
        Mockito.when(mockedPushNotifications.generateToken(Mockito.anyString()))
                .thenThrow(new PusherTooManyRequestsError("test"));

        try {

            // When
            beamsClient.generateToken(userId);
            Assertions.fail("Expecting a PusherTooManyRequestsError to be thrown for this test");

        } catch (PusherTooManyRequestsError e) {

            // Then
            Mockito.verify(mockedPushNotifications, Mockito.times(expectedAttempts))
                    .generateToken(userId);
        }
    }

    @Test
    public void rateLimitedInterceptor_shouldHandle_publishToInterests_operation()
            throws Throwable {

        // Given
        final int expectedAttempts = config.rateLimit.maxRetry;
        final List<String> users = Arrays.asList("test");
        final Map<String, Object> publishData = new HashMap<String, Object>();

        Mockito.when(
                mockedPushNotifications.publishToInterests(Mockito.anyList(), Mockito.anyMap()))
                .thenThrow(new PusherTooManyRequestsError("test"));

        try {

            // When
            beamsClient.publishToInterests(users, publishData);
            Assertions.fail("Expecting a PusherTooManyRequestsError to be thrown for this test");

        } catch (PusherTooManyRequestsError e) {

            // Then
            Mockito.verify(mockedPushNotifications, Mockito.times(expectedAttempts))
                    .publishToInterests(users, publishData);
        }
    }

    @Test
    public void rateLimitedInterceptor_shouldHandle_publishToUsers_operation() throws Throwable {

        // Given
        final int expectedAttempts = config.rateLimit.maxRetry;
        final List<String> users = Arrays.asList("test");
        final Map<String, Object> publishData = new HashMap<String, Object>();

        Mockito.when(mockedPushNotifications.publishToUsers(Mockito.anyList(), Mockito.anyMap()))
                .thenThrow(new PusherTooManyRequestsError("test"));

        try {

            // When
            beamsClient.publishToUsers(users, publishData);
            Assertions.fail("Expecting a PusherTooManyRequestsError to be thrown for this test");

        } catch (PusherTooManyRequestsError e) {

            // Then
            Mockito.verify(mockedPushNotifications, Mockito.times(expectedAttempts))
                    .publishToUsers(users, publishData);
        }
    }
}

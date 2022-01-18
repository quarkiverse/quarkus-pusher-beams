package io.quarkiverse.pusher.beams.server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pusher.pushnotifications.PushNotifications;

import io.quarkiverse.pusher.beams.notification.PublishRequest;

@ExtendWith(MockitoExtension.class)
public class BeamsClientTest {
    private static final String TEST_USER_ID = "test";

    @Mock
    PushNotifications mockedPushNotification;

    @Mock
    PushNotificationsFactory mockedFactory;

    @InjectMocks
    BeamsClient beamsClient = new BeamsClient();

    @BeforeEach
    public void setup() {

        Mockito.when(mockedFactory.generate())
                .thenReturn(mockedPushNotification);

        beamsClient.setup();
    }

    @Test
    public void deleteUser_should_delegateToPusherSdk() {

        Mockito.verifyNoInteractions(mockedPushNotification);

        beamsClient.deleteUser(TEST_USER_ID);

        Mockito.verify(mockedPushNotification, Mockito.times(1))
                .deleteUser(TEST_USER_ID);
    }

    @Test
    public void generateToken_should_delegateToPusherSdk() {

        Mockito.verifyNoInteractions(mockedPushNotification);

        beamsClient.generateToken(TEST_USER_ID);

        Mockito.verify(mockedPushNotification, Mockito.times(1))
                .generateToken(TEST_USER_ID);
    }

    @Test
    public void publishToUsers_should_delegateToPusherSdk()
            throws IOException, InterruptedException, URISyntaxException {

        Mockito.verifyNoInteractions(mockedPushNotification);
        final List<String> users = Arrays.asList(TEST_USER_ID);
        final PublishRequest request = new PublishRequest();

        beamsClient.publishToUsers(users, request);

        Mockito.verify(mockedPushNotification, Mockito.times(1))
                .publishToUsers(users, request);
    }

    @Test
    public void publishToInterests_should_delegateToPusherSdk()
            throws IOException, InterruptedException, URISyntaxException {

        Mockito.verifyNoInteractions(mockedPushNotification);
        final List<String> users = Arrays.asList(TEST_USER_ID);
        final PublishRequest request = new PublishRequest();

        beamsClient.publishToInterests(users, request);

        Mockito.verify(mockedPushNotification, Mockito.times(1))
                .publishToInterests(users, request);
    }

}

# Quarkus - Pusher Beams
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-1-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->

This extension brings the [Pusher Push Notification Server java library](https://github.com/pusher/push-notifications-server-java) into the Quarkus ecosystem. 

It offers an injectable proxy bean called BeamsClient that provide the same functionalities as the PushNotifications object provided by Pusher SDK.

The extension also provide additional features like a prepared Java bean to publish notification (read the documentation below for restrictions) and an automated retry system in case of rate limitation rejections from Pusher.

## Features

The extension provide the same features as the original Pusher library. For more information about it, please refers to the [Pusher Java/Kotlin documentation](https://pusher.com/docs/beams/reference/server-sdk-java-kotlin/)

The extension also provides the following additional features.

**CDI injection**

You can inject the BeamsClient in your application & use it to interact with your Pusher Beams instance.

**Retry on HTTP 429 responses from Pusher**

As you may know, Pusher has rate limits on its services. The rate limit will depends on your subscription model.

As the BeamsClient wrap the call to the Pusher SDK, it also catch any PusherTooManyRequestsError returned and will automatically sleep for a configurable delay and try to re-submit your request again.
It will do that until either the request could be successfully submitted, or another critical error is returned / thrown by the SDK, or the number of retry exceed the maximum number of attempts configured on the extension.

To customize this retry behavior, please refers to the configuration section below.

## How to install

The quarkus-pusher-beams jar is available in Maven Central.

```xml
<dependencies>
    <dependency>
      <groupId>io.quarkiverse.pusher.beams</groupId>
      <artifactId>quarkus-pusher-beams</artifactId>
      <version>[YOUR VERSION HERE]</version>
    </dependency>
</dependencies>
```

## How to configure


<details><summary>Enabling / disabling the extension</summary>
<p>

The extension is enabled by default as long as you include it into your dependencies.
To disable it through configuration, please use the following property.

```
quarkus.pusher.beams.enabled=false
```

</p>
</details>

<details><summary>Configure Pusher Beams instance ID</summary>
<p>

This is a mandatory property. Set the Pusher Beams instance ID to connect to.

```
quarkus.pusher.beams.instance-id=8f9a6e22-2483-49aa-8552-125f1a4c5781
```

</p>
</details>

<details><summary>Configure Pusher Beams instance secret key</summary>
<p>

This is a mandatory property. Set the Pusher Beams instance secret key to connect to.

```
quarkus.pusher.beams.secret-key=C54D42FB7CD2D408DDB22D7A0166F1D
```

</p>
</details>

<details><summary>Configure rate limit behavior - maximum retry</summary>
<p>

Configures the maximum number of attempt the extension will try to re-submit your Pusher operation (i.e publish to users, delete a user, etc.) in case of PusherTooManyRequestError.

**Optional**

**Default value is 5**.

```
quarkus.pusher.beams.rate-limit.max-retry=3
```

</p>
</details>

<details><summary>Configure rate limit behavior - delay</summary>
<p>

Configures the delay in milliseconds between attempts in case of PusherTooManyRequestError.


**Optional**

**Default value is 20 milliseconds**.

```
quarkus.pusher.beams.rate-limit.delay=100
```

</p>
</details>


## How to use


<details><summary>Publish to users (Apple)</summary>
<p>

Here is an example to publish to users using the PublishRequest bean.
The publishToUsers function explicitly throw some exceptions that you'll have to handle but be aware that the Pusher SDK also thrown several runtime exceptions that you may be interested in handling as well depending on your use case.

```java

@ApplicationScoped
public class AppleNotificationExample {

  @Inject
  BeamsClient beamsClient;
  
  public void publish(final List<String> users, final String title, final String subtitle, final String body) {
  
    // Use the PublishRequest object or build your own custom Map as publish request
    final PublishRequest publishRequest = new PublishRequest();
    publishRequest.apns()
                .aps()
                .withBadge(1)
                .withMutableContent();
    publishRequest.apns()
                .aps()
                .alert()
                .withTitle(title)
                .withSubtitle(subtitle)
                .withBody(StringEscapeUtils.unescapeJava(body));
    publishRequest.apns()
                .pusher()
                .withDisableDeliveryTracking();
    
    try {
    
      beamsClient.publishToUsers(users, publishRequest);
      
    } catch (IOException | InterruptedException | URISyntaxException e) {
    
      // Unexpected exception handling here
      
    } catch (PusherAuthError | PusherTooManyRequestsError | PusherMissingInstanceError | PusherValidationError | PusherServerError e) {
    
      // Pusher runtime exception handling here
    }
  }
}

```

</p>
</details>


<details><summary>Delete a user</summary>
<p>

Here is an example to delete a user.
The deleteUser function does not explicitly throw any exception but be aware that the Pusher SDK also thrown several runtime exceptions that you may be interested in handling as well depending on your use case.

```java

@ApplicationScoped
public class DeleteUserExample {

  @Inject
  BeamsClient beamsClient;
  
  public void delete(final String userId) {
  
    try {
    
      beamsClient.deleteUser(userId);
      
    } catch (PusherAuthError | PusherTooManyRequestsError | PusherMissingInstanceError | PusherValidationError | PusherServerError e) {
    
      // Pusher runtime exception handling here
    }
  }
}

```

</p>
</details>


<details><summary>Generate a token</summary>
<p>

Here is an example to generate a token.

```java

@ApplicationScoped
public class GenerateTokenExample {

  @Inject
  BeamsClient beamsClient;
  
  public void generate(final String userId) {
  
      Map<String, Object> tokenInfo = beamsClient.generateToken(userId);
      
      // Do anything you need with token information
      // Usually this is used on your own authentication endpoint to authenticate Pusher beams users.
  }
}

```

</p>
</details>

## How to contribute to this extension

This extension should provide all you need to interact with your Pusher Beams instance and use the Pusher Beam server SDK.

But if you notice anything wrong, want to submit additional features or cover more things inside the existing features, please submit your remarks and/or PR.

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="https://github.com/nicolas-vivot"><img src="https://avatars.githubusercontent.com/u/79290619?v=4?s=100" width="100px;" alt=""/><br /><sub><b>nvivot</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-pusher-beams/commits?author=nicolas-vivot" title="Code">💻</a> <a href="#maintenance-nicolas-vivot" title="Maintenance">🚧</a></td>
  </tr>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!

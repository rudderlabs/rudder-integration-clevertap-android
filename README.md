# What is RudderStack?

[RudderStack](https://rudderstack.com/) is a **customer data pipeline tool** for collecting, routing and processing data from your websites, apps, cloud tools, and data warehouse.

With RudderStack, you can build customer data pipelines that connect your whole customer data stack and then make them smarter by triggering enrichment and activation in customer tools based on analysis in your data warehouse. Its easy-to-use SDKs and event source integrations, Cloud Extract integrations, transformations, and expansive library of destination and warehouse integrations makes building customer data pipelines for both event streaming and cloud-to-warehouse ELT simple.

| Try **RudderStack Cloud Free** - a no time limit, no credit card required, completely free tier of [RudderStack Cloud](https://resources.rudderstack.com/rudderstack-cloud). Click [here](https://app.rudderlabs.com/signup?type=freetrial) to start building a smarter customer data pipeline today, with RudderStack Cloud Free. |
|:------|

Questions? Please join our [Slack channel](https://resources.rudderstack.com/join-rudderstack-slack) or read about us on [Product Hunt](https://www.producthunt.com/posts/rudderstack).

## Integrating CleverTap with the RudderStack Android SDK

1. Add [CleverTap](https://clevertap.com) as a destination in the [RudderStack dashboard](https://app.rudderstack.com/).

2. Add the following lines to your ```app/build.gradle```
```groovy
repositories {
  mavenCentral()
}
```
3. Add the following dependencies under ```dependencies```
```groovy
implementation 'com.rudderstack.android.sdk:core:1+'
implementation 'com.rudderstack.android.integration:clevertap:1+'
```

## Initializing ```RudderClient```

```kotlin
val rudderClient: RudderClient = RudderClient.getInstance(
    this,
    WRITE_KEY,
    RudderConfig.Builder()
        .withDataPlaneUrl(DATA_PLANE_URL)
        .withFactory(CleverTapIntegrationFactory.FACTORY)
        .build()
)
```

## Sending Events

Follow the steps from our [RudderStack Android SDK](https://github.com/rudderlabs/rudder-sdk-android#send-events) repo.

## Contact Us

If you come across any issues while configuring or using this integration, please feel free to start a conversation on our [Slack](https://resources.rudderstack.com/join-rudderstack-slack) channel. We will be happy to help you.

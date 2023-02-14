package com.rudderstack.android.sample.kotlin

import android.app.Application
import com.rudderstack.android.integrations.clevertap.CleverTapIntegrationFactory
import com.rudderstack.android.sdk.core.RudderClient
import com.rudderstack.android.sdk.core.RudderConfig
import com.rudderstack.android.sdk.core.RudderLogger

class MainApplication : Application() {
    companion object {
        private const val WRITE_KEY = "2LMFf1X4aYWSrE7m9rYsvUjJUO6"
//        private const val CONTROL_PLANE_URL = "https://c7d361b63320.ngrok.io"
        lateinit var rudderClient: RudderClient
    }

    override fun onCreate() {
        super.onCreate()
        rudderClient = RudderClient.getInstance(
            this,
            WRITE_KEY,
            RudderConfig.Builder()
//                .withControlPlaneUrl(CONTROL_PLANE_URL)
                .withDataPlaneUrl("https://rudderstacbumvdrexzj.dataplane.rudderstack.com")
                .withTrackLifecycleEvents(false)
                .withFactory(CleverTapIntegrationFactory.FACTORY)
                .withLogLevel(RudderLogger.RudderLogLevel.NONE)
                .build()
        )
    }
}
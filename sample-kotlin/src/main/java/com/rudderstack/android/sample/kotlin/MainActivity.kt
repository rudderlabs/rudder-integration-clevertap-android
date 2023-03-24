package com.rudderstack.android.sample.kotlin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rudderlabs.android.sample.kotlin.R
import com.rudderstack.android.sdk.core.RudderProperty
import com.rudderstack.android.sdk.core.RudderTraits
import com.rudderstack.android.sample.kotlin.MainApplication.Companion.rudderClient
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onButtonTap(view: View) {
        when (view.id) {
            R.id.btn ->
                rudderClient.identify(
                    "rudderstack_android_4"
                )
            R.id.btn2 ->
                rudderClient.identify(
                    "rudderstack_android_4",
                    RudderTraits()
                        .putBirthday(Date(631172471000))
                        .putAddress(RudderTraits.Address()
                            .putCity("Palo Alto")
                            .putCountry("USA"))
                        .putName("RudderStack Android")
                        .putAge("28")
                        .putGender("Male")
                        .putPhone("+919123456789")
                        .putEmail("android-4@rudderstack.com")
                        .put("key-1", "value-1")
                        .put("key-2", 1234)
                        .put("Tz", "Asia/Kolkata"),
                    null
                )
            R.id.btn3 ->
                rudderClient.track("Order Completed")
            R.id.btn4 ->
                rudderClient.track("Order Completed", RudderProperty()
                    .putValue("revenue", 123)
                    .putValue("currency", "INR")
                    .putValue("Key-1", "Value-1")
                )
            R.id.btn5 ->
                rudderClient.track("Order Completed", RudderProperty()
                    .putValue("revenue", 123)
                    .putValue("currency", "INR")
                    .putValue("Key-1", "Value-1")
                    .putValue("order_id", "1234567890")
                )
            R.id.btn6 ->
                rudderClient.track("Order Completed", RudderProperty()
                    .putValue(
                        mapOf(Pair("products",
                            listOf(mapOf<String, Any>(
                                Pair("product_id", "1001"),
                                Pair("quantity", 11),
                                Pair("price", 100.11),
                                Pair("Product-Key-1", "Product-Value-1"))
                            )
                        ))
                    )
                    .putValue("revenue", 123)
                    .putValue("currency", "INR")
                    .putValue("Key-1", "Value-1")
                )
            R.id.btn7 ->
                rudderClient.track("Order Completed", RudderProperty()
                    .putValue(
                        mapOf(Pair("products",
                            listOf(mapOf<String, Any>(
                                Pair("product_id", "1001"),
                                Pair("quantity", 11),
                                Pair("price", 100.11),
                                Pair("Product-Key-1", "Product-Value-1"))
                            )
                        ))
                    )
                    .putValue("revenue", 123)
                    .putValue("currency", "INR")
                    .putValue("Key-1", "Value-1")
                    .putValue("order_id", "1234567890")
                )
            R.id.btn8 ->
                rudderClient.track("Order Completed", RudderProperty()
                    .putValue(
                        mapOf(Pair("products",
                            listOf(
                                mapOf<String, Any>(
                                    Pair("product_id", "1002"),
                                    Pair("quantity", 12),
                                    Pair("price", 100.22)),
                                mapOf<String, Any>(
                                    Pair("product_id", "1003"),
                                    Pair("quantity", 5),
                                    Pair("price", 89.50))
                            )
                        ))
                    )
                    .putValue("revenue", 123)
                    .putValue("currency", "INR")
                    .putValue("Key-1", "Value-1")
                    .putValue("order_id", "1234567890")
                )
            R.id.btn9 ->
                rudderClient.track("New Track event", RudderProperty()
                    .putValue("key_1", "value_1")
                    .putValue("key_2", "value_2")
                )
            R.id.btn10 ->
                rudderClient.track("New Track event")
            R.id.btn11 ->
                rudderClient.track("Home", RudderProperty()
                    .putValue("key_1", "value_1")
                    .putValue("key_2", "value_2")
                )
            R.id.btn12 ->
                rudderClient.track("Home")
        }
    }
}

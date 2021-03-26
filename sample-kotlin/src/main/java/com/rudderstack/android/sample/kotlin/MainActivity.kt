package com.rudderstack.android.sample.kotlin

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.rudderlabs.android.sample.kotlin.R
import com.rudderstack.android.sdk.core.RudderProperty
import com.rudderstack.android.sdk.core.RudderTraits
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // For Android Version starting from O we need to create a channel and use its ID while configuring push notifications on clevertap dashboard
//        CleverTapAPI.createNotificationChannel(
//            getApplicationContext(),
//            "channelId",
//            "Your Channel Name",
//            "Your Channel Description",
//            NotificationManager.IMPORTANCE_MAX,
//            true
//        );
        var identify = findViewById<Button>(R.id.identify);
        identify.setOnClickListener {
            MainApplication.rudderClient.identify(
                "sairoop",
                RudderTraits()
                    .putEmail("sairoop@gmail.com")
                    .putFirstName("Nalluri")
                    .putLastName("SaiRoop")
                    .putName("Nalluri SaiRoop")
                    .putPhone("+919919769943"),
                null
            )
        }
        var track = findViewById<Button>(R.id.track);
        track.setOnClickListener {

            val payload = RudderProperty()
            val productsArray = JSONArray()
            val product1 = JSONObject()
            product1.put("product_id", 123)
            product1.put("sku", "G-32")
            product1.put("name", "Monopoly")
            product1.put("price", 14)
            product1.put("quantity", 1)
            product1.put("category", "Games")
            product1.put("url", "https://www.website.com/product/path")
            product1.put("image_url", "https://www.website.com/product/path.jpg")
            val product2 = JSONObject()
            product2.put("product_id", 345)
            product2.put("sku", "F-32")
            product2.put("name", "UNO")
            product2.put("price", 3.45)
            product2.put("quantity", 2)
            product2.put("category", "Games")
            product2.put("url", "https://www.website.com/product/path")
            product2.put("image_url", "https://www.website.com/product/path.jpg")
            productsArray.put(product1)
            productsArray.put(product2)

            payload.put("order_id", 1234)
            payload.put("affiliation", "Apple Store")
            payload.put("value", 20)
            payload.put("revenue", 15.0032345)
            payload.put("shipping", 22)
            payload.put("tax", 1)
            payload.put("discount", 1.5)
            payload.put("coupon", "ImagePro")
            payload.put("currency", "USD")
            payload.put("products", productsArray)
            MainApplication.rudderClient.track("Order Completed", payload);

        }
        var screen = findViewById<Button>(R.id.screen);
        screen.setOnClickListener {
            MainApplication.rudderClient.screen(localClassName)
        }


    }
}

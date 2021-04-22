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
//        For Android Version starting from O we need to create a channel and use its ID while configuring push notifications on clevertap dashboard
//        CleverTapAPI.createNotificationChannel(
//            getApplicationContext(),
//            "channelId",
//            "Your Channel Name",
//            "Your Channel Description",
//            NotificationManager.IMPORTANCE_MAX,
//            true
//        );

//        TC:1 Track call only with event name before identify
//        MainApplication.rudderClient.track("Sample event before identify.");

//        TC:2 Identify call only with userId
//        MainApplication.rudderClient.identify("android00001");

//        TC:3 Identify call with the same userId and update traits
//        val traits = RudderTraitsBuilder();
//        traits.setName("Ansuman Saha");
//        traits.setPostalCode("700065");
//        traits.setCountry("India");
//        traits.setState("West Bengal");
//        traits.setCity("Kolkata");
//        traits.setId("android00001");
//        MainApplication.rudderClient.identify(traits);


//        TC:4 Track call with event name and property after Identify
//        MainApplication.rudderClient.track("Sample event after identify.",
//            RudderProperty()
//                .putValue("Colour","Black")
//                .putValue("Weight","25lb"));


//        TC:5 Identify with anonymousId, email, and name
//        val traits1 = RudderTraits();
//        traits1.putName("Arpan Saha");
//        traits1.putEmail("hahaha@gmail.com");
//        MainApplication.rudderClient.identify(traits1);


//        TC:6 Screen with identified anonymousId, name and properties
//        MainApplication.rudderClient.screen("Flipkart Page",
//            RudderProperty()
//                .putValue("url","www.flipkart.com")
//                .putValue("referer","facebook")
//                .putValue("height",5))


//        TC:7 Identify call with userId, email, phone, gender, education, married, employed, name
//        val traits2 = RudderTraits();
//        val address = RudderTraits.Address();
//        address.putCity("Kolkata");
//        address.putCountry("India");
//        address.putState("West Bengal");
//        address.putPostalCode("700065");
//        traits2.putId("android00002");
//        traits2.putEmail("android00002@gmail.com");
//        traits2.putPhone("+918855226777");
//        traits2.putGender("M");
//        traits2.put("Education","Graduate");
//        traits2.put("Married","N");
//        traits2.put("Employed","Y");
//        traits2.putName("Pratap Saha");
//        traits2.putAddress(address);
//        MainApplication.rudderClient.identify(traits2);


//        TC:8 Identify call with the wrong email and phone number without country code.
//        val traits3 = RudderTraits();
//        traits3.putId("android00003");
//        traits3.putName("Gourav Saha");
//        traits3.putEmail("abcdefghijklm");
//        traits3.putPhone("9874563210");
//        MainApplication.rudderClient.identify(traits3);


//        TC:9 Identify call with gender : M/m/Male/male, F/f/Female/female
//        val traits4 = RudderTraitsBuilder();
//        traits4.setName("Sourav Saha");
//        traits4.setGender("m");
//        traits4.setId("android00004");
//        MainApplication.rudderClient.identify(traits4);

//        val traits5 = RudderTraitsBuilder();
//        traits5.setName("Sourav2 Saha");
//        traits5.setGender("male");
//        traits5.setId("android00005");
//        MainApplication.rudderClient.identify(traits5);

//        val traits6 = RudderTraitsBuilder();
//        traits6.setName("Sourav3 Saha");
//        traits6.setGender("Male");
//        traits6.setId("android00006");
//        MainApplication.rudderClient.identify(traits6);

//        val traits7 = RudderTraitsBuilder();
//        traits7.setName("Sourava1 Saha");
//        traits7.setGender("F");
//        traits7.setId("android00007");
//        MainApplication.rudderClient.identify(traits7);

//        val traits8 = RudderTraitsBuilder();
//        traits8.setName("Sourava2 Saha");
//        traits8.setGender("Female");
//        traits8.setId("android00008");
//        MainApplication.rudderClient.identify(traits8);

//        val traits9 = RudderTraitsBuilder();
//        traits9.setName("Sourava3 Saha");
//        traits9.setGender("female");
//        traits9.setId("android00009");
//        MainApplication.rudderClient.identify(traits9);

//        val traits10 = RudderTraitsBuilder();
//        traits10.setName("Sourava4 Saha");
//        traits10.setGender("f");
//        traits10.setId("android000010");
//        MainApplication.rudderClient.identify(traits10);

//        TC:10 Identify call with birthday  : Date/String
//        val traits11 = RudderTraits();
//        traits11.putBirthday("1998-11-28");
//        traits11.putId("android000011");
//        MainApplication.rudderClient.identify(traits11);

//        val traits12 = RudderTraits();
//        print(Date());
//        traits12.putBirthday(Date());
//        traits12.putId("android000012");
//        MainApplication.rudderClient.identify(traits12);

//        TC:11 Identify call with Array of array/ Array of object/ Object of object/
//        array of integers/ Array of strings/ simple key value pair(key as a
//        string and value as int or string).

//        val traits13 = RudderTraits();
//        var arr = arrayOf(arrayOf("Marks","25","65"), arrayOf("Priority:", "Jeans", "T-Shirt"));
//        traits13.put("Data",arr);
//        traits13.putId("android000013");
//        MainApplication.rudderClient.identify(traits13);

//        class myclass1 {
//            var x = "Marks";
//            var value = 25;
//        }
//
//        class myclass2 {
//            var x = "Marks";
//            var value = 25;
//        }
//        var temp1=myclass1();
//        var temp2=myclass2();
//        var arr1= arrayOf(temp1,temp2);
//        val traits15= RudderTraits();
//        traits15.put("Data",arr1);
//        traits15.putId("android000014");
//        MainApplication.rudderClient.identify(traits15);

//        class myclass3{
//            var temp1=myclass1();
//            var temp2=myclass2();
//        }
//        val traits16= RudderTraits();
//        traits16.put("Data",myclass3());
//        traits16.putId("android000015");
//        MainApplication.rudderClient.identify(traits16);

//        var arr3= intArrayOf(10,20,30,40);
//        val traits17= RudderTraits();
//        traits17.put("Marks",arr3);
//        traits17.putId("android000016");
//        MainApplication.rudderClient.identify(traits17);

//        var arr4= arrayOf("RED","GREEN","BLACK","YELLOW");
//        val traits18= RudderTraits();
//        traits18.put("Favourite_Colour",arr4);
//        traits18.putId("android000017");
//        MainApplication.rudderClient.identify(traits18);

//        val traits19 = RudderTraits();
//        traits19.put("Favourite_colour","RED");
//        traits19.putId("android000018");
//        MainApplication.rudderClient.identify(traits19);

//        TC:12 Identify call with email
//        val traits14 = RudderTraits();
//        traits14.putId("android000019@gmail.com");
//        traits14.putName("Sourav14 Saha");
//        MainApplication.rudderClient.identify(traits14);

//        TC:13 Track call with name and properties with array of strings/integers
//        val myArray3 = arrayOf<String>("Red","Green","Black");
//        MainApplication.rudderClient.track("Sample event with name and array of strings.",
//                RudderProperty().putValue("colour",myArray3));

//        val intarray = arrayOf<Int>(10,20,30,40);
//        MainApplication.rudderClient.track("Sample event with name and array of integer.",
//            RudderProperty().putValue("size",intarray));
//
//        TC:14 Screen call with name
//        MainApplication.rudderClient.screen("Flipkart Page");
//
//        TC:15 Screen call with name, category and property
//        MainApplication.rudderClient.screen(
//            "Myntra_home",
//            "Mail-T-Shirts",
//            RudderProperty().putValue("Dynamic", "true").putValue("Colour", "Grey"),
//            null
//        );

    }
}

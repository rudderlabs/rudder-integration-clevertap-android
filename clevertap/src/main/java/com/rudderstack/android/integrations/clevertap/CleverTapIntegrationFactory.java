package com.rudderstack.android.integrations.clevertap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.clevertap.android.sdk.CleverTapAPI;
import com.google.gson.Gson;
import com.rudderstack.android.sdk.core.MessageType;
import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderIntegration;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class CleverTapIntegrationFactory
        extends RudderIntegration<CleverTapAPI> {

    private static final String CLEVERTAP_KEY = "CleverTap";
    private final CleverTapAPI cleverTap;

    private final HashMap<String, String> CLEVERTAP_TRAITS_MAPPING = new HashMap<String, String>() {{
        put("name", "Name");
        put("phone", "Phone");
        put("email", "Email");
        put("id", "Identity");
    }};
    private final Set<String> MALE_KEYS = new HashSet<>(Arrays.asList("M",
            "MALE"));
    private final Set<String> FEMALE_KEYS = new HashSet<>(Arrays.asList("F",
            "FEMALE"));


    public static Factory FACTORY = new Factory() {
        @Override
        public RudderIntegration<?> create(
                Object settings,
                RudderClient client,
                RudderConfig rudderConfig
        ) {
            return new CleverTapIntegrationFactory(settings);
        }

        @Override
        public String key() {
            return CLEVERTAP_KEY;
        }
    };

    private CleverTapIntegrationFactory(Object config) {
        Gson gson = new Gson();
        CleverTapDestinationConfig destinationConfig = gson.fromJson(gson.toJson(config), CleverTapDestinationConfig.class);
        if (!destinationConfig.region.equals("none")) {
            CleverTapAPI.changeCredentials(destinationConfig.accountId, destinationConfig.passcode, destinationConfig.region);
        } else {
            CleverTapAPI.changeCredentials(destinationConfig.accountId, destinationConfig.passcode);
        }
        cleverTap = CleverTapAPI.getDefaultInstance(RudderClient.getApplication());
        RudderLogger.logInfo("Initialized Clever Tap SDK");

        RudderClient.getApplication().registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onActivityStopped(@NonNull Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @Nullable Bundle bundle) {
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onActivityStarted(@NonNull Activity activity) {
            }
        });

    }
//  handling life cycle methods of an Application


    private void processRudderEvent(RudderMessage element) {
        String type = element.getType();
        if (type != null) {
            switch (type) {
                case MessageType.IDENTIFY:
                    String userId = element.getUserId();
                    if (!TextUtils.isEmpty(userId)) {
                        Map<String, Object> traits = transformTraits(element.getTraits());
                        try {
                            cleverTap.onUserLogin(traits);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case MessageType.TRACK:
                    String eventName = element.getEventName();
                    try {
                        if (eventName != null) {
                            Map<String, Object> eventProperties = element.getProperties();
                            if (eventName.equals("Order Completed") && eventProperties != null) {
                                handleEcommerceEvent(eventProperties);
                                return;
                            }
                            if (eventProperties == null || eventProperties.size() == 0) {
                                cleverTap.pushEvent(eventName);
                                return;
                            }
                            cleverTap.pushEvent(eventName, eventProperties);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case MessageType.SCREEN:
                    String screenName = element.getEventName();
                    Map<String, Object> screenProperties = element.getProperties();
                    if (screenProperties != null) {
                        cleverTap.pushEvent(String.format("Viewed %s screen", screenName), screenProperties);
                        return;
                    }
                    cleverTap.pushEvent(String.format("Viewed %s screen", screenName));
                    break;
                default:
                    RudderLogger.logWarn("MessageType is not specified or supported");
                    break;
            }
        }
    }


    @Override
    public void reset() {
    }

    @Override
    public void dump(@Nullable RudderMessage element) {
        try {
            if (cleverTap == null) {
                RudderLogger.logDebug("CleverTap instance is null");
                return;
            }
            if (element != null) {
                processRudderEvent(element);
            }
        } catch (Exception e) {
            RudderLogger.logError(e);
        }
    }

    @Override
    public CleverTapAPI getUnderlyingInstance() {
        return this.cleverTap;
    }

    private Map<String, Object> transformTraits(Map<String, Object> traits) {

        Map<String, Object> transformedTraits = new HashMap<>();

        if (traits != null) {
            for (Map.Entry<String, Object> entry : traits.entrySet()) {
                if (CLEVERTAP_TRAITS_MAPPING.containsKey(entry.getKey())) {
                    transformedTraits.put(CLEVERTAP_TRAITS_MAPPING.get(entry.getKey()), entry.getValue());
                    continue;
                }
                transformedTraits.put(entry.getKey(), entry.getValue());
            }
        }

        if (transformedTraits.containsKey("gender")) {
            if (MALE_KEYS.contains(((String) transformedTraits.get("gender")).toUpperCase())) {
                transformedTraits.put("Gender", "M");
            } else if (FEMALE_KEYS.contains(((String) transformedTraits.get("gender")).toUpperCase())) {
                transformedTraits.put("Gender", "F");
            }
            transformedTraits.remove("gender");
        }

        if (transformedTraits.containsKey("birthday")) {
            Date DOB = dateFromString((String) transformedTraits.get("birthday"));
            if (DOB != null) {
                transformedTraits.put("DOB", DOB);
            }
            transformedTraits.remove("birthday");
        }

        return transformedTraits;
    }

    private Date dateFromString(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            return formatter.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    private void handleEcommerceEvent(Map<String, Object> eventProperties) {
        HashMap<String, Object> chargeDetails = new HashMap<>();
        if (eventProperties.containsKey("revenue")) {
            chargeDetails.put("Amount", getRevenue(eventProperties.get("revenue")));
            eventProperties.remove("revenue");
        }
        if (eventProperties.containsKey("order_id")) {
            chargeDetails.put("Charged ID", (String) eventProperties.get("order_id"));
            eventProperties.remove("order_id");
        }
        for (Map.Entry<String, Object> entry : eventProperties.entrySet()) {
            if (!entry.getKey().equals("products")) {
                chargeDetails.put(entry.getKey(), entry.getValue());
            }
        }
        ArrayList<HashMap<String, Object>> items = getProductsList(eventProperties);
        cleverTap.pushChargedEvent(chargeDetails, items);
    }


    private double getRevenue(Object val) {
        String str = String.valueOf(val);
        return Double.valueOf(str);
    }

    private ArrayList<HashMap<String, Object>> getProductsList(Map<String, Object> eventProperties) {
        JSONArray products = null;
        ArrayList<HashMap<String, Object>> productsList = new ArrayList<>();
        if (eventProperties != null) {
            if (eventProperties.containsKey("products")) {
                products = getJSONArray(eventProperties.get("products"));
            }
        }
        try {
            if (products != null && products.length() > 0) {
                for (int i = 0; i < products.length(); i++) {
                    JSONObject product = (JSONObject) products.get(i);
                    HashMap<String, Object> item = new HashMap<>();
                    if (product.get("productId") != null) {
                        item.put("id", product.get("productId"));
                    } else if (product.get("product_id") != null) {
                        item.put("id", product.get("product_id"));
                    }
                    if (product.get("name") != null) {
                        item.put("name", product.get("name"));
                    }
                    if (product.get("sku") != null) {
                        item.put("sku", product.get("sku"));
                    }
                    if (product.get("price") != null) {
                        item.put("price", product.get("price"));
                    }
                    productsList.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productsList;
    }

    private JSONArray getJSONArray(Object object) {
        if (object instanceof JSONArray) {
            return (JSONArray) object;
        } else {
            // if the object received was ArrayList
            return new JSONArray((ArrayList) object);
        }
    }
}

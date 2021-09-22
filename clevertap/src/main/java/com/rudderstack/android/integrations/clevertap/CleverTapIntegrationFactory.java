package com.rudderstack.android.integrations.clevertap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.clevertap.android.sdk.CleverTapAPI;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.rudderstack.android.sdk.core.MessageType;
import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderIntegration;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class CleverTapIntegrationFactory extends RudderIntegration<CleverTapAPI> {
    private static final String CLEVERTAP_KEY = "CleverTap";
    private CleverTapAPI cleverTap = null;

    private final HashMap<String, String> CLEVERTAP_TRAITS_MAPPING = new HashMap<String, String>() {
        {
            put("name", "Name");
            put("phone", "Phone");
            put("email", "Email");
            put("id", "Identity");
        }
    };
    private final Set<String> MALE_KEYS = new HashSet<>(
            Arrays.asList("M", "MALE")
    );
    private final Set<String> FEMALE_KEYS = new HashSet<>(
            Arrays.asList("F", "FEMALE")
    );

    public static Factory FACTORY = new Factory() {
        @Override
        public RudderIntegration<?> create(
                Object settings,
                RudderClient client,
                RudderConfig rudderConfig
        ) {
            return new CleverTapIntegrationFactory(settings, rudderConfig);
        }

        @Override
        public String key() {
            return CLEVERTAP_KEY;
        }
    };

    private CleverTapIntegrationFactory(
            Object config,
            RudderConfig rudderConfig
    ) {
        if (RudderClient.getApplication() == null) {
            RudderLogger.logError("Application is null. Aborting CleverTap initialization.");
            return;
        }

        Gson gson = new Gson();
        CleverTapDestinationConfig destinationConfig = gson.fromJson(
                gson.toJson(config),
                CleverTapDestinationConfig.class
        );
        if (TextUtils.isEmpty(destinationConfig.accountToken) || TextUtils.isEmpty(destinationConfig.accountId)) {
            RudderLogger.logError("Invalid CleverTap Account Credentials, Aborting");
            return;
        }
        if (!destinationConfig.region.equals("none")) {
            CleverTapAPI.changeCredentials(
                    destinationConfig.accountId,
                    destinationConfig.accountToken,
                    destinationConfig.region
            );
        } else {
            CleverTapAPI.changeCredentials(
                    destinationConfig.accountId,
                    destinationConfig.accountToken
            );
        }
        this.cleverTap = CleverTapAPI.getDefaultInstance(RudderClient.getApplication());
        if (rudderConfig.getLogLevel() >= RudderLogger.RudderLogLevel.DEBUG) {
            CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG);
        } else {
            CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.INFO);
        }
        RudderLogger.logInfo("Initialized Clever Tap SDK");

        RudderClient
                .getApplication()
                .registerActivityLifecycleCallbacks(
                        new Application.ActivityLifecycleCallbacks() {
                            @Override
                            public void onActivityCreated(
                                    Activity activity,
                                    Bundle savedInstanceState
                            ) {
                                if (cleverTap == null) {
                                    return;
                                }

                                CleverTapAPI.setAppForeground(true);
                                try {
                                    cleverTap.pushNotificationClickedEvent(activity.getIntent().getExtras());
                                } catch (Exception e) {
                                    RudderLogger.logError(e);
                                }

                                try {
                                    Intent intent = activity.getIntent();
                                    Uri data = intent.getData();
                                    cleverTap.pushDeepLink(data);
                                } catch (Exception e) {
                                    RudderLogger.logError(e);
                                }
                            }

                            @Override
                            public void onActivityResumed(Activity activity) {
                                if (cleverTap == null) {
                                    return;
                                }
                                try {
                                    CleverTapAPI.onActivityResumed(activity);
                                } catch (Exception e) {
                                    RudderLogger.logError(e);
                                }
                            }

                            @Override
                            public void onActivityPaused(Activity activity) {
                                if (cleverTap == null) {
                                    return;
                                }
                                try {
                                    CleverTapAPI.onActivityPaused();
                                } catch (Exception e) {
                                    RudderLogger.logError(e);
                                }
                            }

                            @SuppressLint("RestrictedApi")
                            @Override
                            public void onActivityStopped(@NonNull Activity activity) {
                                // Nothing to Implement
                            }

                            @Override
                            public void onActivitySaveInstanceState(
                                    @NonNull Activity activity,
                                    @Nullable Bundle bundle
                            ) {
                                // Nothing to implement
                            }

                            @Override
                            public void onActivityDestroyed(@NonNull Activity activity) {
                                // Nothing to implement
                            }

                            @SuppressLint("RestrictedApi")
                            @Override
                            public void onActivityStarted(@NonNull Activity activity) {
                                // Nothing to implement
                            }
                        }
                );
    }

    //  handling life cycle methods of an Application
    private void processRudderEvent(RudderMessage element) {
        String type = element.getType();
        if (type != null) {
            switch (type) {
                case MessageType.IDENTIFY:
                    Map<String, Object> traits = transformTraits(element.getTraits());
                    try {
                        this.cleverTap.onUserLogin(traits);
                    } catch (Exception e) {
                        RudderLogger.logError(e);
                    }
                    break;
                case MessageType.TRACK:
                    String eventName = element.getEventName();
                    try {
                        if (eventName != null) {
                            Map<String, Object> eventProperties = element.getProperties();
                            if (
                                    eventName.equals("Order Completed") && eventProperties != null
                            ) {
                                handleECommerceEvent(eventProperties);
                                return;
                            }
                            if (eventProperties == null || eventProperties.size() == 0) {
                                this.cleverTap.pushEvent(eventName);
                                return;
                            }
                            this.cleverTap.pushEvent(eventName, eventProperties);
                        }
                    } catch (Exception e) {
                        RudderLogger.logError(e);
                    }
                    break;
                case MessageType.SCREEN:
                    String screenName = element.getEventName();
                    Map<String, Object> screenProperties = element.getProperties();
                    if (screenProperties != null) {
                        this.cleverTap.pushEvent(
                                String.format("Screen Viewed: %s", screenName),
                                screenProperties
                        );
                        return;
                    }
                    this.cleverTap.pushEvent(String.format("Screen Viewed: %s", screenName));
                    break;
                default:
                    RudderLogger.logWarn("MessageType is not specified or supported");
                    break;
            }
        }
    }

    @Override
    public void reset() {
        // nothing to do
    }

    @Override
    public void dump(@Nullable RudderMessage element) {
        try {
            if (this.cleverTap == null) {
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
                    transformedTraits.put(
                            CLEVERTAP_TRAITS_MAPPING.get(entry.getKey()),
                            entry.getValue()
                    );
                    continue;
                }
                if (entry.getKey().equals("address") || entry.getKey().equals("company")) {
                    LinkedTreeMap<String, String> linkedMap = (LinkedTreeMap<String, String>) entry.getValue();
                    for (Map.Entry<String, String> linkedMapEntry : linkedMap.entrySet()) {
                        if (linkedMapEntry.getKey().equals("id")) {
                            transformedTraits.put("companyId", linkedMapEntry.getValue());
                            continue;
                        }
                        if (linkedMapEntry.getKey().equals("name")) {
                            transformedTraits.put("companyName", linkedMapEntry.getValue());
                            continue;
                        }
                        transformedTraits.put(linkedMapEntry.getKey(), linkedMapEntry.getValue());
                    }
                    continue;
                }
                transformedTraits.put(entry.getKey(), entry.getValue());
            }
        }

        if (transformedTraits.containsKey("gender")) {
            if (transformedTraits.get("gender") instanceof String) {
                String gender = (String) transformedTraits.get("gender");
                if (gender != null) {
                    if (MALE_KEYS.contains(gender.toUpperCase())) {
                        transformedTraits.put("Gender", "M");
                    } else if (FEMALE_KEYS.contains(gender.toUpperCase())) {
                        transformedTraits.put("Gender", "F");
                    }
                }
                transformedTraits.remove("gender");
            }
        }

        if (transformedTraits.containsKey("birthday")) {
            if (transformedTraits.get("birthday") instanceof Date) {
                transformedTraits.put("DOB", transformedTraits.get("birthday"));
            } else if (transformedTraits.get("birthday") instanceof String) {
                Date DOB = dateFromString((String) transformedTraits.get("birthday"));
                if (DOB != null) {
                    transformedTraits.put("DOB", DOB);
                }
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
            RudderLogger.logError(e);
            return null;
        }
    }

    private void handleECommerceEvent(Map<String, Object> eventProperties) {
        HashMap<String, Object> chargeDetails = new HashMap<>();
        if (eventProperties.containsKey("revenue")) {
            chargeDetails.put("Amount", getRevenue(eventProperties.get("revenue")));
            eventProperties.remove("revenue");
        }

        if (eventProperties.containsKey("order_id")) {
            chargeDetails.put("Charged ID", eventProperties.get("order_id"));
            eventProperties.remove("order_id");
        }

        for (Map.Entry<String, Object> entry : eventProperties.entrySet()) {
            if (!entry.getKey().equals("products")) {
                chargeDetails.put(entry.getKey(), entry.getValue());
            }
        }

        ArrayList<HashMap<String, Object>> items = getProductsList(eventProperties);
        this.cleverTap.pushChargedEvent(chargeDetails, items);
    }

    private double getRevenue(Object val) {
        if (val != null) {
            String str = String.valueOf(val);
            return Double.parseDouble(str);
        }
        return 0;
    }

    private ArrayList<HashMap<String, Object>> getProductsList(
            Map<String, Object> eventProperties
    ) {
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

                    // productId
                    if (product.has("productId")) {
                        item.put("id", product.get("productId"));
                    } else {
                        if (product.has("product_id")) {
                            product.get("product_id");
                            item.put("id", product.get("product_id"));
                        }
                    }

                    // product name
                    if (product.has("name")) {
                        product.get("name");
                        item.put("name", product.get("name"));
                    }

                    // SKU
                    if (product.has("sku")) {
                        product.get("sku");
                        item.put("sku", product.get("sku"));
                    }

                    // price
                    if (product.has("price")) {
                        product.get("price");
                        item.put("price", product.get("price"));
                    }

                    // finally add the product
                    productsList.add(item);
                }
            }
        } catch (Exception e) {
            RudderLogger.logError(e);
        }
        return productsList;
    }

    private JSONArray getJSONArray(@Nullable Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof JSONArray) {
            return (JSONArray) object;
        }
        if(object instanceof List){
            ArrayList<Object> arrayList = new ArrayList<>((Collection<?>) object);
            return new JSONArray(arrayList);
        }
        try {
            return new JSONArray((ArrayList) object);
        } catch (Exception e) {
            RudderLogger.logDebug("Error while converting the products array to JSONArray type");
        }
        return null;
    }
}

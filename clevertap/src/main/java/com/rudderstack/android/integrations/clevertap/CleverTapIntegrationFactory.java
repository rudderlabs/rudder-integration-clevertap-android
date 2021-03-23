package com.rudderstack.android.integrations.clevertap;

import androidx.annotation.Nullable;
import com.rudderstack.android.sdk.core.RudderClient;
import com.rudderstack.android.sdk.core.RudderConfig;
import com.rudderstack.android.sdk.core.RudderIntegration;
import com.rudderstack.android.sdk.core.RudderLogger;
import com.rudderstack.android.sdk.core.RudderMessage;

public class CleverTapIntegrationFactory
  extends RudderIntegration<RudderClient> {

  private static final String CLEVERTAP_KEY = "CleverTap";

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
  ) {}

  @Override
  public void reset() {}

  @Override
  public void dump(@Nullable RudderMessage element) {
    try {
      if (element != null) {}
    } catch (Exception e) {
      RudderLogger.logError(e);
    }
  }
}

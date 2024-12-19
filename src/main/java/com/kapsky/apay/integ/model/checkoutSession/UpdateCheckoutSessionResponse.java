package com.kapsky.apay.integ.model.checkoutSession;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class UpdateCheckoutSessionResponse {
    String checkoutSessionId;
    String amazonPayRedirectUrl;
}

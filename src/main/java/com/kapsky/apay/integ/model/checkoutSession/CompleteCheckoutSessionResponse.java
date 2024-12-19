package com.kapsky.apay.integ.model.checkoutSession;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CompleteCheckoutSessionResponse {
    String checkoutSessionId;
    String chargePermissionId;
    String chargeId;
}

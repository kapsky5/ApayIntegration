package com.kapsky.apay.integ.model.checkoutSession;

import com.kapsky.apay.integ.model.Amount;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class UpdateCheckoutSessionRequest {
    Amount chargeAmount;
    String checkoutSessionId;
}

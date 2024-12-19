package com.kapsky.apay.integ.model.checkoutSession;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Builder
@Data
public class CreateCheckoutSessionResponse {
    String checkoutSessionId;
}

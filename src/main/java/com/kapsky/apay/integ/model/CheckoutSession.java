package com.kapsky.apay.integ.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CheckoutSession {

    String checkoutSessionId;
}

package com.kapsky.apay.integ.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Builder
@Data
public class CheckoutSession {

    String checkoutSessionId;
}

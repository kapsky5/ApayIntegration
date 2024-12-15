package com.kapsky.apay.integ.controller;

import com.kapsky.apay.integ.model.CreateCheckoutSessionRequest;
import com.kapsky.apay.integ.model.CreateCheckoutSessionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkoutSession")
public class CheckoutSessionController {

    @PostMapping
    public ResponseEntity<CreateCheckoutSessionResponse> createCheckoutSession(final CreateCheckoutSessionRequest createCheckoutSessionRequest) {

        return ResponseEntity.ok(CreateCheckoutSessionResponse.builder()
                        .checkoutSessionId("123")
                        .build());
    }

}

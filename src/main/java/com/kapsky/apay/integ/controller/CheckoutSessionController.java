package com.kapsky.apay.integ.controller;

import com.kapsky.apay.integ.model.CreateCheckoutSessionRequest;
import com.kapsky.apay.integ.model.CreateCheckoutSessionResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkoutSession")
@Log4j2
public class CheckoutSessionController {

    @PostMapping
    public ResponseEntity<CreateCheckoutSessionResponse> createCheckoutSession(
            final CreateCheckoutSessionRequest createCheckoutSessionRequest) {
        log.info("CreateCheckoutSession Request: {}", createCheckoutSessionRequest);
        return ResponseEntity.ok(CreateCheckoutSessionResponse.builder()
                        .checkoutSessionId("123")
                        .build());
    }

}

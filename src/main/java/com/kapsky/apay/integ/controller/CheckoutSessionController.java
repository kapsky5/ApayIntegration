package com.kapsky.apay.integ.controller;

import com.amazon.pay.api.AmazonPayResponse;
import com.amazon.pay.api.WebstoreClient;
import com.amazon.pay.api.exceptions.AmazonPayClientException;
import com.kapsky.apay.integ.model.checkoutSession.CompleteCheckoutSessionRequest;
import com.kapsky.apay.integ.model.checkoutSession.CompleteCheckoutSessionResponse;
import com.kapsky.apay.integ.model.checkoutSession.CreateCheckoutSessionRequest;
import com.kapsky.apay.integ.model.checkoutSession.CreateCheckoutSessionResponse;
import com.kapsky.apay.integ.model.checkoutSession.UpdateCheckoutSessionRequest;
import com.kapsky.apay.integ.model.checkoutSession.UpdateCheckoutSessionResponse;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/checkoutSession")
@Log4j2
public class CheckoutSessionController {

    @Autowired
    private WebstoreClient webstoreClient;

    @Value("${apay.merchant.store.id}")
    private String storeId;

    @PostMapping
    public ResponseEntity<CreateCheckoutSessionResponse> createCheckoutSession(
            @RequestBody CreateCheckoutSessionRequest createCheckoutSessionRequest) {

        log.info("CreateCheckoutSession Request :: {}", createCheckoutSessionRequest);

        Map<String,String> headers = buildHeaderForCheckoutRequest();
        JSONObject payload = buildCreateCheckoutSessionPayload(createCheckoutSessionRequest);
        AmazonPayResponse response = null;
        try {
            response = webstoreClient.createCheckoutSession(payload, headers);
            log.info("CreateCheckoutSession Response :: {}", response);
        } catch (AmazonPayClientException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(CreateCheckoutSessionResponse.builder()
                        .checkoutSessionId(response.getResponse().getString("checkoutSessionId"))
                        .build());
    }

    @PostMapping("{checkoutSessionId}")
    public ResponseEntity<UpdateCheckoutSessionResponse> updateCheckoutSession(
            @RequestBody UpdateCheckoutSessionRequest updateCheckoutSessionRequest) {

        log.info("UpdateCheckoutSession Request :: {}", updateCheckoutSessionRequest);

        JSONObject payload = buildUpdateCheckoutSessionRequest(updateCheckoutSessionRequest);
        AmazonPayResponse response = null;
        try {
            response = webstoreClient.updateCheckoutSession(updateCheckoutSessionRequest.getCheckoutSessionId(), payload);
            log.info("UpdateCheckoutSession Response :: {}", response);
        } catch (AmazonPayClientException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(UpdateCheckoutSessionResponse.builder()
                .checkoutSessionId(response.getResponse().getString("checkoutSessionId"))
                .amazonPayRedirectUrl(response.getResponse().getJSONObject("webCheckoutDetails").getString("amazonPayRedirectUrl"))
                .build());
    }

    @PostMapping("/complete")
    public ResponseEntity<CompleteCheckoutSessionResponse> completeCheckoutSession(
            @RequestBody final CompleteCheckoutSessionRequest completeCheckoutSessionRequest) {

        log.info("CompleteCheckoutSession Request :: {}", completeCheckoutSessionRequest);

        Map<String,String> headers = buildHeaderForCheckoutRequest();
        JSONObject payload = buildCompleteCheckoutSessionRequest(completeCheckoutSessionRequest);
        AmazonPayResponse response = null;
        try {
            response = webstoreClient.completeCheckoutSession(completeCheckoutSessionRequest.getCheckoutSessionId(), payload);
            log.info("CompleteCheckoutSession Response :: {}", response);
        } catch (AmazonPayClientException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(CompleteCheckoutSessionResponse.builder()
                .checkoutSessionId(response.getResponse().getString("checkoutSessionId"))
                .chargePermissionId(response.getResponse().getString("chargePermissionId"))
                .chargeId(response.getResponse().getString("chargeId"))
                .build());
    }

    private JSONObject buildCompleteCheckoutSessionRequest(final CompleteCheckoutSessionRequest completeCheckoutSessionRequest) {
        log.info("Building CompleteCheckoutSession request:");
        JSONObject payload = new JSONObject();
        // ChargeAmount
        JSONObject chargeAmount = new JSONObject();
        chargeAmount.put("amount", completeCheckoutSessionRequest.getChargeAmount().getAmount());
        chargeAmount.put("currencyCode", completeCheckoutSessionRequest.getChargeAmount().getCurrency());
        payload.put("chargeAmount", chargeAmount);

        log.info("CreateCheckoutSession Request generated: {}", payload);
        return payload;
    }

    private JSONObject buildUpdateCheckoutSessionRequest(UpdateCheckoutSessionRequest updateCheckoutSessionRequest) {
        log.info("Building UpdateCheckoutSession request");
        JSONObject payload = new JSONObject();
        // PaymentIntent
        JSONObject paymentDetails = new JSONObject();
        JSONObject chargeAmount = new JSONObject();
        chargeAmount.put("amount", updateCheckoutSessionRequest.getChargeAmount().getAmount());
        chargeAmount.put("currencyCode", updateCheckoutSessionRequest.getChargeAmount().getCurrency());
        paymentDetails.put("chargeAmount", chargeAmount);
        payload.put("paymentDetails", paymentDetails);

        log.info("CreateCheckoutSession Request generated: {}", payload);
        return payload;
    }

    private JSONObject buildCreateCheckoutSessionPayload(final CreateCheckoutSessionRequest createCheckoutSessionRequest) {
        log.info("Building CreateCheckoutSession request: {}", createCheckoutSessionRequest);
        JSONObject payload = new JSONObject();
        JSONObject webCheckoutDetails = new JSONObject();
        webCheckoutDetails.put("checkoutResultReturnUrl", "https://www.google.com/");
        webCheckoutDetails.put("checkoutReviewReturnUrl", "https://www.google.com/");
        payload.put("webCheckoutDetails", webCheckoutDetails);
        payload.put("storeId", storeId);

        // PaymentIntent
        JSONObject paymentDetails = new JSONObject();
        paymentDetails.put("paymentIntent", createCheckoutSessionRequest.getPaymentIntent());
        paymentDetails.put("canHandlePendingAuthorization", "false");

        JSONObject chargeAmount = new JSONObject();
        chargeAmount.put("amount", createCheckoutSessionRequest.getChargeAmount().getAmount());
        chargeAmount.put("currencyCode", createCheckoutSessionRequest.getChargeAmount().getCurrency());
        paymentDetails.put("chargeAmount", chargeAmount);
        payload.put("paymentDetails", paymentDetails);

        // ChargePermission and PMOF Metadata
        JSONObject pmofMetada = new JSONObject();
        pmofMetada.put("setupOnly", "false");
        payload.put("chargePermissionType", "PaymentMethodOnFile");
        payload.put("paymentMethodOnFileMetadata", pmofMetada);

        log.info("CreateCheckoutSession Request generated: {}", payload);
        return payload;
    }

    private Map<String, String> buildHeaderForCheckoutRequest() {
        Map<String,String> header = new HashMap<>();
        header.put("x-amz-pay-idempotency-key", UUID.randomUUID().toString().replace("-", ""));
        return header;
    }
}

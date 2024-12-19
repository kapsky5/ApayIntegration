package com.kapsky.apay.integ.configuration;

import com.amazon.pay.api.PayConfiguration;
import com.amazon.pay.api.WebstoreClient;
import com.amazon.pay.api.exceptions.AmazonPayClientException;
import com.amazon.pay.api.types.Environment;
import com.amazon.pay.api.types.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class APayConfigurations {

    @Value("${apay.merchant.private.key.path}")
    private String privateKeyPath;

    @Value("${apay.merchant.public.key.id}")
    private String publicKeyId;

    @Bean
    public PayConfiguration getAPayConfiguration() throws IOException, AmazonPayClientException {
        return new PayConfiguration()
                .setPublicKeyId(publicKeyId)
                .setRegion(Region.EU)
                .setPrivateKey(new String(Files.readAllBytes(Paths.get(privateKeyPath))).toCharArray())
                .setEnvironment(Environment.SANDBOX)
                .setAlgorithm("AMZN-PAY-RSASSA-PSS-V2");
    }

    @Bean
    public WebstoreClient getWebstoreClient(final PayConfiguration payConfiguration) throws AmazonPayClientException {
        return new WebstoreClient(payConfiguration);
    }

}

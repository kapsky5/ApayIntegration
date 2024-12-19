package com.kapsky.apay.integ.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Amount {
    Long amount;
    String currency;
}

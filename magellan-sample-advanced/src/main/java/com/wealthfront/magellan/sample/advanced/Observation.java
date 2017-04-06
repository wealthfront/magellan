package com.wealthfront.magellan.sample.advanced;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
class Observation {

    @JsonProperty(value = "v")
    BigDecimal verifiedWaterLevel;

    public Observation() {}

    public BigDecimal getVerifiedWaterLevel() {
        return verifiedWaterLevel;
    }

}

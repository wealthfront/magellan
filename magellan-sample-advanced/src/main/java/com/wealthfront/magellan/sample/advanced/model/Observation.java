package com.wealthfront.magellan.sample.advanced.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Observation {

  @JsonProperty(value = "v")
  BigDecimal verifiedWaterLevel;

  public Observation() {}

  public BigDecimal getVerifiedWaterLevel() {
    return verifiedWaterLevel;
  }

}

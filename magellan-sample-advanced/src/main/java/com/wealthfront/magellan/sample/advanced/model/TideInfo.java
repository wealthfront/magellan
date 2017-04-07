package com.wealthfront.magellan.sample.advanced.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TideInfo {

  @JsonProperty
  List<Observation> data;

  public TideInfo() {}

  public List<Observation> getData() {
    return data;
  }

}

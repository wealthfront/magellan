package com.wealthfront.magellan.sample.advanced.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TideInfo {

  @JsonProperty
  List<Observation> data;

  public TideInfo() {}

  public static Builder with() {
    return new Builder();
  }

  public List<Observation> getData() {
    return data;
  }

  public static class Builder {

    List<Observation> data;

    public Builder data(List<Observation> data) {
      this.data = data;
      return this;
    }

    public TideInfo build() {
      TideInfo tideInfo = new TideInfo();
      tideInfo.data = data;
      return tideInfo;
    }

  }

}

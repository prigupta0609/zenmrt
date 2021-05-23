package com.zendesk.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class Segment {

    @JsonProperty("originDestination")
    @NotNull
    private OriginDestination originDestination;
    @JsonProperty("lineCode")
    @NotNull
    private String lineCode;

    public Segment() {}

    public Segment(OriginDestination originDestination, String lineCode) {
        this.originDestination = originDestination;
        this.lineCode = lineCode;
    }

    @JsonProperty("originDestination")
    public OriginDestination getOriginDestination() {
        return originDestination;
    }

    @JsonProperty("lineCode")
    public String getLineCode() {
        return lineCode;
    }
}

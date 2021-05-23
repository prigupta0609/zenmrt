package com.zendesk.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class OriginDestination {

    @JsonProperty("originName")
    @NotNull
    private String originName;
    @JsonProperty("originCode")
    @NotNull
    private String originCode;
    @JsonProperty("destinationName")
    @NotNull
    private String destinationName;
    @JsonProperty("destinationCode")
    @NotNull
    private String destinationCode;

    public OriginDestination() {}

    public OriginDestination(String originName,
                             String originCode,
                             String destinationName,
                             String destinationCode) {
        this.originName = originName;
        this.originCode = originCode;
        this.destinationName = destinationName;
        this.destinationCode = destinationCode;
    }

    @JsonProperty("originName")
    public String getOriginName() {
        return originName;
    }
    @JsonProperty("destinationName")
    public String getDestinationName() {
        return destinationName;
    }
    @JsonProperty("originCode")
    public String getOriginCode() {
        return originCode;
    }
    @JsonProperty("destinationCode")
    public String getDestinationCode() {
        return destinationCode;
    }
}

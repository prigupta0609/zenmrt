package com.zendesk.contract;

import javax.validation.constraints.NotNull;
import java.util.List;

public class OriginDestination {

    @NotNull
    private final String originName;
    @NotNull
    private final List<String> originCode;
    @NotNull
    private final String destinationName;
    @NotNull
    private final List<String> destinationCode;

    public OriginDestination(String originName, List<String> originCode, String destinationName, List<String> destinationCode) {
        this.originName = originName;
        this.originCode = originCode;
        this.destinationName = destinationName;
        this.destinationCode = destinationCode;
    }

    public String getOriginName() {
        return originName;
    }

    public List<String> getOriginCode() {
        return originCode;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public List<String> getDestinationCode() {
        return destinationCode;
    }
}

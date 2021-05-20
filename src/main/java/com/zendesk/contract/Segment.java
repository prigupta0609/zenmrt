package com.zendesk.contract;

import javax.validation.constraints.NotNull;

public class Segment {

    @NotNull
    private final OriginDestination originDestination;
    @NotNull
    private final String lineCode;

    public Segment(OriginDestination originDestination, String lineCode) {
        this.originDestination = originDestination;
        this.lineCode = lineCode;
    }

    public OriginDestination getOriginDestination() {
        return originDestination;
    }

    public String getLineCode() {
        return lineCode;
    }
}

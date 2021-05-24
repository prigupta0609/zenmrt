package com.zendesk.model;

import javax.validation.constraints.NotNull;

public class StationCode {

    @NotNull
    private final String line;
    @NotNull
    private final String code;

    /**
     * completeStationCode contains line and code collectively
     * for example: NW01
     * Assuming that first line is always a 2-letter code
     */
    public StationCode(String completeStationCode) {
        this.line = completeStationCode.substring(0, 2);
        this.code = completeStationCode;
    }

    public String getLine() {
        return line;
    }

    public String getCode() {
        return code;
    }
}

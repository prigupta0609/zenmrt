package com.zendesk.model;

import org.joda.time.LocalDateTime;

public class Station {

    private final String name;
    private final StationCode code;
    private final LocalDateTime startDate;

    public Station(String name, StationCode code, LocalDateTime startDate) {
        this.name = name;
        this.code = code;
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public StationCode getCode() {
        return code;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }
}

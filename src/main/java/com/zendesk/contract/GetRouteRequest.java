package com.zendesk.contract;

import org.joda.time.LocalDateTime;

public class GetRouteRequest {

    private String origin;
    private String destination;
    private LocalDateTime date;

    // TODO : Improvise object builder logic
    public GetRouteRequest(String origin, String destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}

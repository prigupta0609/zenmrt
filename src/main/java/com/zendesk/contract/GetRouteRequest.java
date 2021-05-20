package com.zendesk.contract;

import java.util.Date;

public class GetRouteRequest {

    private String origin;
    private String destination;
    private Date date;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

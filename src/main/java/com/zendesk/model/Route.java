package com.zendesk.model;

import java.util.List;

public class Route {

    private final List<Station> route;

    public Route(List<Station> route) {
        this.route = route;
    }

    public List<Station> getRoute() {
        return route;
    }
}
